package io.github.shogowada.scalajs.reactjs.redux

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object Action {
  def actionFromNative(nativeAction: js.Dynamic): Action = nativeAction.wrapped.asInstanceOf[Action]

  def actionToNative(action: Action): js.Dynamic = js.Dynamic.literal(
    "type" -> action.getClass.getSimpleName,
    "wrapped" -> action.asInstanceOf[js.Any]
  )
}

trait Action

@js.native
trait Store extends js.Object

object Redux {
  type NativeDispatch = js.Function1[js.Dynamic, js.Dynamic]
  type Dispatch = Action => Action

  def createStore[State](reducer: (Option[State], Action) => State): Store = {
    NativeRedux.createStore((state: js.Object, nativeAction: js.Dynamic) => {
      if (js.isUndefined(state)) {
        reducer(None, null).asInstanceOf[js.Object]
      } else {
        val action = Action.actionFromNative(nativeAction)
        reducer(Some(state.asInstanceOf[State]), action).asInstanceOf[js.Object]
      }
    })
  }
}

@js.native
@JSImport("redux", JSImport.Namespace)
object NativeRedux extends js.Object {
  def createStore(reducer: js.Function2[js.Object, js.Dynamic, js.Object]): Store = js.native
}
