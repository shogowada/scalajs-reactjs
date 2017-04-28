package io.github.shogowada.scalajs.reactjs.redux

import io.github.shogowada.scalajs.reactjs.redux.Redux.{NativeEnhancer, NativeReducer}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object Action {
  def actionFromNative(nativeAction: js.Dynamic): Option[Action] =
    if (js.isUndefined(nativeAction.wrapped)) {
      None
    } else {
      Option(nativeAction.wrapped.asInstanceOf[Action])
    }

  def actionToNative(action: Action): js.Dynamic = js.Dynamic.literal(
    "type" -> action.getClass.getSimpleName,
    "wrapped" -> action.asInstanceOf[js.Any]
  )
}

trait Action

@js.native
trait NativeStore extends js.Object {
  def getState(): js.Object
}

case class Store[State](native: NativeStore) {
  def getState: State = native.asInstanceOf[js.Dynamic].getState().asInstanceOf[State]
}

object Redux {
  type NativeReducer = js.Function2[js.Object, js.Dynamic, js.Object]
  type NativeEnhancer = js.Function1[js.Function2[NativeReducer, js.Object, NativeStore], js.Function2[NativeReducer, js.Object, NativeStore]]
  type NativeDispatch = js.Function1[js.Dynamic, js.Dynamic]
  type Dispatch = Action => Action

  def createStore[State](reducer: (Option[State], Action) => State): Store[State] =
    createStore(reducer, null)

  def createStore[State](
      reducer: (Option[State], Action) => State,
      enhancer: NativeEnhancer
  ): Store[State] = {
    val nativeStore = NativeRedux.createStore((state: js.Object, nativeAction: js.Dynamic) => {
      if (js.isUndefined(state) || state == null) {
        reducer(None, null).asInstanceOf[js.Object]
      } else {
        Action.actionFromNative(nativeAction) match {
          case Some(action: Action) =>
            reducer(Some(state.asInstanceOf[State]), action)
                .asInstanceOf[js.Object]
          case _ => state
        }
      }
    }, enhancer)
    Store(nativeStore)
  }
}

@js.native
@JSImport("redux", JSImport.Namespace)
object NativeRedux extends js.Object {
  def createStore(reducer: NativeReducer, enhancer: NativeEnhancer): NativeStore = js.native
}
