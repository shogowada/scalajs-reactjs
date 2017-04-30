package io.github.shogowada.scalajs.reactjs.router.redux

import io.github.shogowada.scalajs.history.History
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.Redux.{Middleware, NativeMiddleware, Reducer}
import io.github.shogowada.scalajs.reactjs.redux.{NativeAction, ReduxInternal}

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSImport, JSName}

@js.native
@JSImport("react-router-redux", JSImport.Namespace)
object ReactRouterReduxAction extends js.Object {
  @JSName("push")
  def Push(path: String): NativeAction = js.native
  @JSName("replace")
  def Replace(path: String): NativeAction = js.native
  @JSName("go")
  def Go(delta: Int): NativeAction = js.native
  @JSName("goBack")
  def GoBack(): NativeAction = js.native
  @JSName("goForward")
  def GoForward(): NativeAction = js.native
}

object ReactRouterRedux {
  implicit class RouterReduxVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val ConnectedRouter = elements(NativeReactRouterRedux.ConnectedRouter)
  }

  val routerReducer: Reducer[js.Object] =
    (state: Option[_], action: Any) => {
      val nativeState: js.Object = state.map(_.asInstanceOf[js.Object]).getOrElse(js.Object())
      val nativeAction = ReduxInternal.actionToNative(action)
      NativeReactRouterRedux.routerReducer(nativeState, nativeAction)
    }

  def routerMiddleware[State](history: History): Middleware[State] =
    ReduxInternal.middlewareFromNative(
      NativeReactRouterRedux.routerMiddleware(history)
    )
}

@js.native
@JSImport("react-router-redux", JSImport.Namespace)
object NativeReactRouterRedux extends js.Object {
  val ConnectedRouter: ReactClass = js.native

  def routerReducer(routerState: js.Object, action: NativeAction): js.Object = js.native
  def routerMiddleware(history: History): NativeMiddleware = js.native
}
