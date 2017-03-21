package io.github.shogowada.scalajs.reactjs.redux

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSImport, ScalaJSDefined}

trait Action

@ScalaJSDefined
class ActionWrapper(action: Action) extends js.Object {
  val `type`: String = action.getClass.getSimpleName
  val wrapped: Action = action
}

@js.native
trait Store extends js.Object

object Redux {
  type NativeDispatch = js.Function1[ActionWrapper, ActionWrapper]
  type Dispatch = Action => Action

  def createStore[State](reducer: (Option[State], Action) => State): Store = {
    NativeRedux.createStore((state: js.Object, nativeAction: js.Object) => {
      if (js.isUndefined(state)) {
        reducer(None, null).asInstanceOf[js.Object]
      } else {
        val action: ActionWrapper = nativeAction.asInstanceOf[ActionWrapper]
        reducer(Some(state.asInstanceOf[State]), action.wrapped).asInstanceOf[js.Object]
      }
    })
  }
}

@js.native
@JSImport("redux", JSImport.Namespace)
object NativeRedux extends js.Object {
  def createStore(reducer: js.Function2[js.Object, js.Object, js.Object]): Store = js.native
}
