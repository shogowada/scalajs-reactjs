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
  def nativeToOwnProps[OwnProps](nativeOwnProps: js.Dynamic): OwnProps = nativeOwnProps.wrapped.asInstanceOf[OwnProps]

  def ownPropsToNative[OwnProps](ownProps: OwnProps): js.Dynamic = js.Dynamic.literal(
    "wrapped" -> ownProps.asInstanceOf[js.Any]
  )
}

class ContainerComponent[OwnProps](wrappedClass: ReactClass) {
  def apply(ownProps: OwnProps)(children: js.Any*): ReactElement = {
    val nativeOwnProps = ContainerComponent.ownPropsToNative(ownProps)
    React.createElement(wrappedClass, nativeOwnProps, children: _*)
  }
}

class PropslessContainerComponent(wrappedClass: ReactClass) {
  def apply(children: js.Any*): ReactElement = {
    React.createElement(wrappedClass, (), children: _*)
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

  def connect[ReduxState, Props, State](
      selector: (Dispatch, ReduxState) => Props
  )(
      classSpec: ReactClassSpec[Props, State]
  ): PropslessContainerComponent = {
    val nativeSelectorFactory: js.Function1[NativeDispatch, js.Function2[ReduxState, js.Dynamic, js.Any]] =
      (nativeDispatch: NativeDispatch) => {
        val dispatch: Dispatch = nativeToDispatch(nativeDispatch)
        (state: ReduxState, ownProps: js.Dynamic) => {
          val props: Props = selector(dispatch, state)
          classSpec.propsToNative(props)
        }
      }

    val nativeContainerComponent: ReactClass =
      NativeReactRedux.connectAdvanced(nativeSelectorFactory)(React.createClass(classSpec))

    new PropslessContainerComponent(nativeContainerComponent)
  }

  def connect[ReduxState, OwnProps, Props, State](
      selector: (Dispatch, ReduxState, OwnProps) => Props
  )(
      classSpec: ReactClassSpec[Props, State]
  ): ContainerComponent[OwnProps] = {
    connectAdvanced(
      (dispatch) => (state: ReduxState, ownProps: OwnProps) => selector(dispatch, state, ownProps)
    )(classSpec)
  }

  def connectAdvanced[ReduxState, OwnProps, Props, State](
      selectorFactory: Dispatch => (ReduxState, OwnProps) => Props
  )(
      classSpec: ReactClassSpec[Props, State]
  ): ContainerComponent[OwnProps] = {
    val nativeSelectorFactory: js.Function1[NativeDispatch, js.Function2[ReduxState, js.Dynamic, js.Any]] =
      (nativeDispatch: NativeDispatch) => {
        val dispatch: Dispatch = nativeToDispatch(nativeDispatch)
        val selector = selectorFactory(dispatch)
        (state: ReduxState, nativeOwnProps: js.Dynamic) => {
          val ownProps: OwnProps = ContainerComponent.nativeToOwnProps(nativeOwnProps)
          val props: Props = selector(state, ownProps)
          classSpec.propsToNative(props)
        }
      }

    val nativeContainerComponent: ReactClass =
      NativeReactRedux.connectAdvanced(nativeSelectorFactory)(React.createClass(classSpec))

    new ContainerComponent[OwnProps](nativeContainerComponent)
  }

  private def nativeToDispatch(nativeDispatch: NativeDispatch): Dispatch =
    (action: Action) => {
      val nativeAction = Action.actionToNative(action)
      Action.nativeToAction(nativeDispatch(nativeAction))
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
