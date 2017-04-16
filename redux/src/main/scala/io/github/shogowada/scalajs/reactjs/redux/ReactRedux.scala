package io.github.shogowada.scalajs.reactjs.redux

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMAttributes.Type.AS_IS
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.classes.specs.ReactClassSpec
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux.ReactReduxVirtualDOMAttributes.StoreAttributeSpec
import io.github.shogowada.scalajs.reactjs.redux.Redux.NativeDispatch
import io.github.shogowada.statictags.{Attribute, AttributeSpec}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object ReactRedux {

  import Redux.Dispatch

  implicit class ReactReduxVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Provider = ReactClassElementSpec(NativeReactReduxProvider)
  }

  object ReactReduxVirtualDOMAttributes {
    case class StoreAttributeSpec(name: String) extends AttributeSpec {
      def :=(store: Store) = Attribute(name, store, AS_IS)
    }
  }

  implicit class ReactReduxVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val store = StoreAttributeSpec("store")
  }

  def connectAdvanced[ReduxState, OwnProps, WrappedProps](
      selectorFactory: Dispatch => (ReduxState, OwnProps) => WrappedProps
  ): ContainerComponentFactory[WrappedProps] = {
    val nativeSelectorFactory = selectorFactoryToNative(selectorFactory)

    val nativeFactory = NativeReactRedux.connectAdvanced(nativeSelectorFactory)
    new ContainerComponentFactory[WrappedProps](nativeFactory)
  }

  private def selectorFactoryToNative[ReduxState, OwnProps, WrappedProps](
      selectorFactory: Dispatch => (ReduxState, OwnProps) => WrappedProps
  ): js.Function1[NativeDispatch, js.Function2[ReduxState, js.Dynamic, js.Any]] =
    (nativeDispatch: NativeDispatch) => {
      val dispatch: Dispatch = dispatchFromNative(nativeDispatch)
      val selector = selectorFactory(dispatch)
      selectorToNative(selector)
    }

  private def selectorToNative[ReduxState, OwnProps, WrappedProps](
      selector: (ReduxState, OwnProps) => WrappedProps
  ): js.Function2[ReduxState, js.Dynamic, js.Any] =
    (state: ReduxState, nativeOwnProps: js.Dynamic) => {
      val ownProps: OwnProps = ReactClassSpec.propsFromNative[OwnProps](nativeOwnProps).wrapped
      val wrappedProps: WrappedProps = selector(state, ownProps)
      val nativeProps = clone(nativeOwnProps)
      nativeProps.updateDynamic(ReactClassSpec.WrappedProperty)(wrappedProps.asInstanceOf[js.Any])
      nativeProps
    }

  private def clone(plainObject: js.Dynamic): js.Dynamic = {
    val clonedPlainObject = js.Dynamic.literal()
    val keys = js.Object.keys(plainObject.asInstanceOf[js.Object])
    keys.foreach(key => clonedPlainObject.updateDynamic(key)(plainObject.selectDynamic(key)))
    clonedPlainObject
  }

  private def dispatchFromNative(nativeDispatch: NativeDispatch): Dispatch =
    (action: Action) => {
      val nativeAction = Action.actionToNative(action)
      val newNativeAction = nativeDispatch(nativeAction)
      Action.actionFromNative(newNativeAction).getOrElse({
        throw new IllegalStateException(s"Expected native action $newNativeAction to be wrapped")
      })
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
