package io.github.shogowada.scalajs.reactjs.redux

import io.github.shogowada.scalajs.reactjs.redux.Redux._

import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.annotation.JSImport

trait Action

@js.native
trait NativeAction extends js.Object {
  val `type`: String
}

@js.native
trait NativeStore extends js.Object {
  def getState(): js.Object = js.native
  def dispatch(action: js.Any): js.Any = js.native
}

case class Store[State](native: NativeStore) {
  def getState: State = native.getState().asInstanceOf[State]
  def dispatch(action: Any): Any = native.dispatch(ReduxInternal.actionToNative(action))
}

object Redux {
  type NativeReducer = js.Function2[js.Any, NativeAction, js.Any]
  type NativeEnhancer = js.Any
  type NativeDispatch = js.Function1[js.Any, js.Any]
  type NativeMiddleware = js.Function1[NativeStore, js.Function1[NativeDispatch, js.Function1[js.Any, _]]]

  type Reducer[State] = (Option[State], Any) => State
  type Dispatch = Any => Any
  type Middleware[State] = (Store[State]) => (Dispatch) => Any => _

  def combineReducers(reducers: Map[String, (_, Action) => _]): Reducer[js.Object] = {
    val nativeReducer: NativeReducer = NativeRedux.combineReducers(
      reducers.map { case (key, value) =>
        key -> ReduxInternal.reducerToNative(value.asInstanceOf[Reducer[_]])
      }.toJSDictionary
    )
    ReduxInternal.reducerFromNative[js.Object](nativeReducer)
  }

  def createStore[State](reducer: Reducer[State]): Store[State] =
    createStore(reducer, null)

  def createStore[State](
      reducer: Reducer[State],
      enhancer: NativeEnhancer
  ): Store[State] = {
    val nativeReducer = ReduxInternal.reducerToNative(reducer)
    val nativeStore = NativeRedux.createStore(nativeReducer, enhancer)
    Store(nativeStore)
  }

  def applyMiddleware[State](middlewares: Middleware[State]*): NativeEnhancer = {
    val nativeMiddlewares = middlewares.map(ReduxInternal.middlewareToNative)
    NativeRedux.applyMiddleware(nativeMiddlewares: _*)
  }
}

object ReduxInternal {
  def actionToNative(action: Any): NativeAction =
    action match {
      case action: Action => js.Dynamic.literal(
        "type" -> action.getClass.getSimpleName,
        "wrapped" -> action.asInstanceOf[js.Any]
      ).asInstanceOf[NativeAction]
      case _ => action.asInstanceOf[NativeAction]
    }

  def actionFromNative(nativeAction: js.Any): Option[Action] = {
    val dynamicAction = nativeAction.asInstanceOf[js.Dynamic]
    if (js.isUndefined(dynamicAction.wrapped)) {
      None
    } else {
      dynamicAction.wrapped match {
        case action: Action => Option(action)
        case _ => None
      }
    }
  }

  def reducerToNative[State](reducer: Reducer[State]): NativeReducer = {
    (state: js.Any, nativeAction: NativeAction) => {
      val maybeState: Option[State] = Option(state)
          .filterNot(js.isUndefined)
          .map(_.asInstanceOf[State])

      actionFromNative(nativeAction) match {
        case Some(action: Action) => reducer(maybeState, action).asInstanceOf[js.Any]
        case _ => reducer(maybeState, nativeAction).asInstanceOf[js.Any]
      }
    }
  }

  def reducerFromNative[State](native: NativeReducer): Reducer[State] = {
    (maybeState: Option[State], action: Any) => {
      val nativeAction = actionToNative(action)
      val nativeState = maybeState match {
        case Some(state) => native(state.asInstanceOf[js.Object], nativeAction)
        case None => native(js.undefined, nativeAction)
      }
      nativeState.asInstanceOf[State]
    }
  }

  def middlewareToNative[State](middleware: Middleware[State]): NativeMiddleware = {
    (nativeStore: NativeStore) => {
      val middleware2 = middleware(Store(nativeStore))
      (nativeDispatch: NativeDispatch) => {
        val middleware3 = middleware2(dispatchFromNative(nativeDispatch))
        (nativeAction: js.Any) => {
          actionFromNative(nativeAction) match {
            case Some(action: Action) => middleware3(action)
            case None => middleware3(nativeAction)
          }
        }
      }
    }
  }

  def middlewareFromNative[State](middleware: NativeMiddleware): Middleware[State] = {
    (store: Store[State]) => {
      val middleware2 = middleware(store.native)
      (dispatch: Dispatch) => {
        val middleware3 = middleware2(dispatchToNative(dispatch))
        (action: Any) => {
          middleware3(actionToNative(action))
        }
      }
    }
  }

  def dispatchToNative(dispatch: Dispatch): NativeDispatch =
    (nativeAction: js.Any) => {
      val result = actionFromNative(nativeAction) match {
        case Some(action: Action) => dispatch(action)
        case None => dispatch(nativeAction)
      }
      if (js.isUndefined(result) || result == null) {
        js.undefined
      } else {
        result.asInstanceOf[js.Any]
      }
    }

  def dispatchFromNative(nativeDispatch: NativeDispatch): Dispatch =
    (action: Any) => action match {
      case action: Action =>
        val nativeAction = actionToNative(action)
        nativeDispatch(nativeAction)
      case _ => nativeDispatch(action.asInstanceOf[js.Any])
    }
}

@js.native
@JSImport("redux", JSImport.Namespace)
object NativeRedux extends js.Object {
  def combineReducers(reducers: js.Dictionary[NativeReducer]): NativeReducer = js.native
  def createStore(reducer: NativeReducer, enhancer: NativeEnhancer): NativeStore = js.native
  def applyMiddleware(middlewares: NativeMiddleware*): NativeEnhancer = js.native
}
