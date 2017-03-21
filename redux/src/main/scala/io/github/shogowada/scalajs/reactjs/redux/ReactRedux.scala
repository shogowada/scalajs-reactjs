package io.github.shogowada.scalajs.reactjs.redux

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.classes.specs.ReactClassSpec
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.redux.Redux.NativeDispatch

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

class ContainerComponent[OwnProps](wrappedClass: ReactClass) {
  def apply(ownProps: OwnProps)(children: js.Any*): ReactElement = {
    React.createElement(wrappedClass, ownProps, children: _*)
  }
}

class ProplessContainerComponent(wrappedClass: ReactClass) {
  def apply(children: js.Any*): ReactElement = {
    React.createElement(wrappedClass, js.Object(), children: _*)
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

  def connect[State, Props](
      selector: (Dispatch, State) => Props
  )(
      classSpec: ReactClassSpec
  ): ProplessContainerComponent = {
    val nativeSelectorFactory: js.Function1[NativeDispatch, js.Function2[State, js.Object, js.Any]] =
      (nativeDispatch: NativeDispatch) => {
        val dispatch: Dispatch = (action: Action) => nativeDispatch(new ActionWrapper(action)).wrapped
        (state: State, ownProps: js.Object) => {
          val props: Props = selector(dispatch, state)
          classSpec.propsToRawJs(props.asInstanceOf[classSpec.Props])
        }
      }

    val nativeContainerComponent: ReactClass =
      NativeReactRedux.connectAdvanced(nativeSelectorFactory)(React.createClass(classSpec))

    new ProplessContainerComponent(nativeContainerComponent)
  }

  def connect[State, OwnProps, Props](
      selector: (Dispatch, State, OwnProps) => Props
  )(
      classSpec: ReactClassSpec
  ): ContainerComponent[OwnProps] = {
    connectAdvanced(
      (dispatch) => (state: State, ownProps: OwnProps) => selector(dispatch, state, ownProps)
    )(classSpec)
  }

  def connectAdvanced[State, OwnProps, Props](
      selectorFactory: Dispatch => (State, OwnProps) => Props
  )(
      classSpec: ReactClassSpec
  ): ContainerComponent[OwnProps] = {
    val nativeSelectorFactory: js.Function1[NativeDispatch, js.Function2[State, OwnProps, js.Any]] =
      (nativeDispatch: NativeDispatch) => {
        val dispatch: Dispatch = (action: Action) => nativeDispatch(new ActionWrapper(action)).wrapped
        val selector = selectorFactory(dispatch)
        (state: State, ownProps: OwnProps) => {
          val props: Props = selector(state, ownProps)
          classSpec.propsToRawJs(props.asInstanceOf[classSpec.Props])
        }
      }

    val nativeContainerComponent: ReactClass =
      NativeReactRedux.connectAdvanced(nativeSelectorFactory)(React.createClass(classSpec))

    new ContainerComponent[OwnProps](nativeContainerComponent)
  }
}

@js.native
@JSImport("react-redux", "Provider")
object NativeReactReduxProvider extends ReactClass

@js.native
@JSImport("react-redux", JSImport.Namespace)
object NativeReactRedux extends js.Object {
  def connectAdvanced[State, OwnProps](selectorFactory: js.Function1[Redux.NativeDispatch, js.Function2[State, OwnProps, js.Any]]): js.Function1[ReactClass, ReactClass] = js.native
}
