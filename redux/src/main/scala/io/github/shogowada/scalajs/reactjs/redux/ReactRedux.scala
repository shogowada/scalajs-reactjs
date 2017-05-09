package io.github.shogowada.scalajs.reactjs.redux

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMAttributes.Type.AS_IS
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux.ReactReduxVirtualDOMAttributes.StoreAttributeSpec
import io.github.shogowada.scalajs.reactjs.redux.Redux.NativeDispatch
import io.github.shogowada.statictags.{Attribute, AttributeSpec}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object ReactRedux {

  import Redux.Dispatch

  implicit class ReactReduxVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Provider = elements(NativeReactReduxProvider)
  }

  object ReactReduxVirtualDOMAttributes {
    case class StoreAttributeSpec(name: String) extends AttributeSpec {
      def :=[State](store: Store[State]) = Attribute(name, store.native, AS_IS)
    }
  }

  implicit class ReactReduxVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val store = StoreAttributeSpec("store")
  }

  def connectAdvanced[ReduxState, OwnProps, WrappedProps](
      selectorFactory: Dispatch => (ReduxState, Props[OwnProps]) => WrappedProps
  ): ContainerComponentFactory[WrappedProps] = {
    val nativeSelectorFactory = selectorFactoryToNative(selectorFactory)

    val nativeFactory = NativeReactRedux.connectAdvanced(nativeSelectorFactory)
    new ContainerComponentFactory[WrappedProps](nativeFactory)
  }

  private def selectorFactoryToNative[ReduxState, OwnProps, WrappedProps](
      selectorFactory: Dispatch => (ReduxState, Props[OwnProps]) => WrappedProps
  ): js.Function1[NativeDispatch, js.Function2[ReduxState, js.Dynamic, js.Any]] =
    (nativeDispatch: NativeDispatch) => {
      val dispatch: Dispatch = ReduxInternal.dispatchFromNative(nativeDispatch)
      val selector = selectorFactory(dispatch)
      selectorToNative(selector)
    }

  private def selectorToNative[ReduxState, OwnProps, WrappedProps](
      selector: (ReduxState, Props[OwnProps]) => WrappedProps
  ): js.Function2[ReduxState, js.Dynamic, js.Any] =
    (state: ReduxState, nativeOwnProps: js.Dynamic) => {
      val ownProps: Props[OwnProps] = Props(nativeOwnProps)
      val wrappedProps: WrappedProps = selector(state, ownProps)
      val nativeProps = clone(nativeOwnProps)
      nativeProps.updateDynamic(React.WrappedProperty)(wrappedProps.asInstanceOf[js.Any])
      nativeProps
    }

  private def clone(plainObject: js.Dynamic): js.Dynamic = {
    val clonedPlainObject = js.Dynamic.literal()
    val keys = js.Object.keys(plainObject.asInstanceOf[js.Object])
    keys.foreach(key => clonedPlainObject.updateDynamic(key)(plainObject.selectDynamic(key)))
    clonedPlainObject
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
