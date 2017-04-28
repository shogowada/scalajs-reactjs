package io.github.shogowada.scalajs.reactjs.redux

import io.github.shogowada.scalajs.reactjs.redux.Redux._

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object Action {
  def actionFromNative(nativeAction: js.Dynamic): Option[Action] =
    if (js.isUndefined(nativeAction.wrapped)) {
      None
    } else {
      nativeAction.wrapped match {
        case action: Action => Option(action)
        case _ => None
      }
    }

  def actionToNative(action: Any): js.Dynamic = action match {
    case action: Action => js.Dynamic.literal(
      "type" -> action.getClass.getSimpleName,
      "wrapped" -> action.asInstanceOf[js.Any]
    )
    case _ => action.asInstanceOf[js.Dynamic]
  }
}

trait Action

@js.native
trait NativeStore extends js.Object {
  def getState(): js.Object = js.native
  def dispatch(action: js.Dynamic): js.Dynamic = js.native
}

case class Store[State](native: NativeStore) {
  def getState: State = native.asInstanceOf[js.Dynamic].getState().asInstanceOf[State]
  def dispatch(action: Any): Any = native.dispatch(Action.actionToNative(action))
}

object Redux {
  type NativeReducer = js.Function2[js.Object, js.Dynamic, js.Object]
  type NativeEnhancer = js.Any
  type NativeDispatch = js.Function1[js.Dynamic, js.Dynamic]
  type NativeMiddleware = js.Function1[NativeStore, js.Function1[NativeDispatch, js.Function1[js.Object, _]]]

  type Dispatch = Any => Any
  type Middleware[State] = (Store[State]) => (Dispatch) => Any => _

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
            reducer(Some(state.asInstanceOf[State]), action).asInstanceOf[js.Object]
          case _ => state
        }
      }
    }, enhancer)
    Store(nativeStore)
  }

  def applyMiddleware[State](middlewares: Middleware[State]*): NativeEnhancer = {
    val nativeMiddlewares = middlewares.map(ReduxInternal.middlewareToNative)
    NativeRedux.applyMiddleware(nativeMiddlewares: _*)
  }
}

object ReduxInternal {
  def middlewareToNative[State](middleware: Middleware[State]): NativeMiddleware = {
    (nativeStore: NativeStore) => {
      val middleware2 = middleware(Store(nativeStore))
      (nativeDispatch: NativeDispatch) => {
        val middleware3 = middleware2(ReduxInternal.dispatchFromNative(nativeDispatch))
        (nativeAction: js.Object) => {
          Action.actionFromNative(nativeAction.asInstanceOf[js.Dynamic]) match {
            case Some(action: Action) => middleware3(action)
            case None => middleware3(nativeAction)
          }
        }
      }
    }
  }

  def dispatchFromNative(nativeDispatch: NativeDispatch): Dispatch =
    (action: Any) => action match {
      case action: Action =>
        val nativeAction = Action.actionToNative(action)
        nativeDispatch(nativeAction)
      case _ => nativeDispatch(action.asInstanceOf[js.Dynamic])
    }
}

@js.native
@JSImport("redux", JSImport.Namespace)
object NativeRedux extends js.Object {
  def createStore(reducer: NativeReducer, enhancer: NativeEnhancer): NativeStore = js.native
  def applyMiddleware(middlewares: NativeMiddleware*): NativeEnhancer = js.native
}
