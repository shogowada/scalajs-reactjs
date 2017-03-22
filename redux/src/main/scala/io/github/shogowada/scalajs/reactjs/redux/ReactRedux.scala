package io.github.shogowada.scalajs.reactjs.redux

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.classes.specs.ReactClassSpec
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

  def connect[Props, State](
      selector: (Dispatch) => Props
  )(
      classSpec: ReactClassSpec[Props, State]
  ): ContainerComponent[Unit] = {
    connect((dispatch: Dispatch, _: js.Any) => selector(dispatch))(classSpec)
  }

  def connect[ReduxState, Props, State](
      selector: (Dispatch, ReduxState) => Props
  )(
      classSpec: ReactClassSpec[Props, State]
  ): ContainerComponent[Unit] = {
    connect((dispatch, state: ReduxState, ownProps: Unit) => selector(dispatch, state))(classSpec)
  }

  def connect[ReduxState, OwnProps, Props, State](
      selector: (Dispatch, ReduxState, OwnProps) => Props
  )(
      classSpec: ReactClassSpec[Props, State]
  ): ContainerComponent[OwnProps] = {
    connectAdvanced(
      (dispatch) => (state: ReduxState, ownProps: OwnProps) =>
        selector(dispatch, state, ownProps)
    )(classSpec)
  }

  def connectAdvanced[ReduxState, OwnProps, Props, State](
      selectorFactory: Dispatch => (ReduxState, OwnProps) => Props
  )(
      classSpec: ReactClassSpec[Props, State]
  ): ContainerComponent[OwnProps] = {
    val nativeSelectorFactory = selectorFactoryToNative(selectorFactory, classSpec)

    val nativeContainerComponent: ReactClass = NativeReactRedux
        .connectAdvanced(nativeSelectorFactory)(React.createClass(classSpec))

    new ContainerComponent[OwnProps](nativeContainerComponent)
  }

  private def selectorFactoryToNative[ReduxState, OwnProps, Props, State](
      selectorFactory: Dispatch => (ReduxState, OwnProps) => Props,
      classSpec: ReactClassSpec[Props, State]
  ): js.Function1[NativeDispatch, js.Function2[ReduxState, js.Dynamic, js.Any]] =
    (nativeDispatch: NativeDispatch) => {
      val dispatch: Dispatch = dispatchFromNative(nativeDispatch)
      val selector = selectorFactory(dispatch)
      selectorToNative(selector, classSpec)
    }

  private def selectorToNative[ReduxState, OwnProps, Props, State](
      selector: (ReduxState, OwnProps) => Props,
      classSpec: ReactClassSpec[Props, State]
  ): js.Function2[ReduxState, js.Dynamic, js.Any] =
    (state: ReduxState, nativeOwnProps: js.Dynamic) => {
      val ownProps: OwnProps = ContainerComponent.ownPropsFromNative(nativeOwnProps)
      val props: Props = selector(state, ownProps)
      propsToNative(classSpec, props, nativeOwnProps)
    }

  private def dispatchFromNative(nativeDispatch: NativeDispatch): Dispatch =
    (action: Action) => {
      val nativeAction = Action.actionToNative(action)
      Action.actionFromNative(nativeDispatch(nativeAction))
    }

  private def propsToNative[Props, State](
      classSpec: ReactClassSpec[Props, State],
      props: Props,
      nativeOwnProps: js.Dynamic
  ): js.Any = {
    val nativeProps = classSpec.propsToNative(props)
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
  def connectAdvanced[State](selectorFactory: js.Function1[Redux.NativeDispatch, js.Function2[State, js.Dynamic, js.Any]]): js.Function1[ReactClass, ReactClass] = js.native
}
