package io.github.shogowada.scalajs.reactjs.redux

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.classes.specs.ReactClassSpec
import io.github.shogowada.scalajs.reactjs.classes.specs.ReactClassSpec.{Renderer, RendererWithChildren}
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.redux.Redux.NativeDispatch

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object ContainerComponent {
  def ownPropsFromNative[OwnProps](nativeOwnProps: js.Dynamic): OwnProps = nativeOwnProps.wrapped.asInstanceOf[OwnProps]

  def ownPropsToNative[OwnProps](ownProps: OwnProps): js.Dynamic = js.Dynamic.literal(
    "wrapped" -> ownProps.asInstanceOf[js.Any]
  )
}

class ContainerComponent[OwnProps](wrappedClass: ReactClass) {
  def apply(children: js.Any*): ReactElement = {
    React.createElement(wrappedClass, (), children: _*)
  }

  def apply(ownProps: OwnProps)(children: js.Any*): ReactElement = {
    val nativeOwnProps = ContainerComponent.ownPropsToNative(ownProps)
    React.createElement(wrappedClass, nativeOwnProps, children: _*)
  }
}

class ContainerComponentFactory[OwnProps, Props](nativeFactory: js.Function1[js.Any, ReactClass]) {
  def apply[State](classSpec: ReactClassSpec[Props, State]): ContainerComponent[OwnProps] = {
    val nativeContainerComponent: ReactClass = nativeFactory(React.createClass(classSpec))
    new ContainerComponent[OwnProps](nativeContainerComponent)
  }

  def apply(renderer: Renderer[Props]): ContainerComponent[OwnProps] = {
    apply((props: Props, children: ReactElement) => renderer(props))
  }

  def apply(renderer: RendererWithChildren[Props]): ContainerComponent[OwnProps] = {
    val nativeRenderer: js.Function1[js.Dynamic, ReactElement] = (nativeProps: js.Dynamic) => {
      val props = ReactClassSpec.propsFromNative[Props](nativeProps)
      val nativeChildren: js.Dynamic = nativeProps.children
      val children: ReactElement = if (js.isUndefined(nativeChildren)) {
        null
      } else {
        nativeChildren.asInstanceOf[ReactElement]
      }
      renderer(props, children)
    }
    val nativeContainerComponent: ReactClass = nativeFactory(nativeRenderer)
    new ContainerComponent[OwnProps](nativeContainerComponent)
  }
}

object ReactRedux {

  import Redux.Dispatch

  implicit class RichVirtualDOMElements(elements: VirtualDOMElements) {
    def Provider(store: Store)(children: js.Any*): ReactElement = {
      val props = js.Dynamic.literal(
        "store" -> store
      )
      React.createElement(NativeReactReduxProvider, props, children: _*)
    }
  }

  def connect[Props](
      selector: (Dispatch) => Props
  ): ContainerComponentFactory[Unit, Props] = {
    connect((dispatch: Dispatch, _: js.Any) => selector(dispatch))
  }

  def connect[ReduxState, Props, State](
      selector: (Dispatch, ReduxState) => Props
  ): ContainerComponentFactory[Unit, Props] = {
    connect((dispatch: Dispatch, state: ReduxState, ownProps: Unit) => selector(dispatch, state))
  }

  def connect[ReduxState, OwnProps, Props, State](
      selector: (Dispatch, ReduxState, OwnProps) => Props
  ): ContainerComponentFactory[OwnProps, Props] = {
    connectAdvanced(
      (dispatch: Dispatch) => (state: ReduxState, ownProps: OwnProps) =>
        selector(dispatch, state, ownProps)
    )
  }

  def connectAdvanced[ReduxState, OwnProps, Props](
      selectorFactory: Dispatch => (ReduxState, OwnProps) => Props
  ): ContainerComponentFactory[OwnProps, Props] = {
    val nativeSelectorFactory = selectorFactoryToNative(selectorFactory)

    val nativeFactory = NativeReactRedux.connectAdvanced(nativeSelectorFactory)
    new ContainerComponentFactory[OwnProps, Props](nativeFactory)
  }

  private def selectorFactoryToNative[ReduxState, OwnProps, Props](
      selectorFactory: Dispatch => (ReduxState, OwnProps) => Props
  ): js.Function1[NativeDispatch, js.Function2[ReduxState, js.Dynamic, js.Any]] =
    (nativeDispatch: NativeDispatch) => {
      val dispatch: Dispatch = dispatchFromNative(nativeDispatch)
      val selector = selectorFactory(dispatch)
      selectorToNative(selector)
    }

  private def selectorToNative[ReduxState, OwnProps, Props](
      selector: (ReduxState, OwnProps) => Props
  ): js.Function2[ReduxState, js.Dynamic, js.Any] =
    (state: ReduxState, nativeOwnProps: js.Dynamic) => {
      val ownProps: OwnProps = ContainerComponent.ownPropsFromNative(nativeOwnProps)
      val props: Props = selector(state, ownProps)
      propsToNative(props, nativeOwnProps)
    }

  private def dispatchFromNative(nativeDispatch: NativeDispatch): Dispatch =
    (action: Action) => {
      val nativeAction = Action.actionToNative(action)
      Action.actionFromNative(nativeDispatch(nativeAction))
    }

  private def propsToNative[Props](
      props: Props,
      nativeOwnProps: js.Dynamic
  ): js.Any = {
    val nativeProps = ReactClassSpec.propsToNative(props)
    nativeProps.children = nativeOwnProps.children
    nativeProps.params = nativeOwnProps.params
    nativeProps
  }

}

@js.native
@JSImport("react-redux", "Provider")
object NativeReactReduxProvider extends ReactClass

@js.native
@JSImport("react-redux", JSImport.Namespace)
object NativeReactRedux extends js.Object {
  def connectAdvanced[State](selectorFactory: js.Function1[Redux.NativeDispatch, js.Function2[State, js.Dynamic, js.Any]]): js.Function1[js.Any, ReactClass] = js.native
}
