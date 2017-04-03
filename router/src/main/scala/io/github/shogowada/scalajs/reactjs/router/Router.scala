package io.github.shogowada.scalajs.reactjs.router

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.classes.specs.ReactClassSpec
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.{React, VirtualDOM}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@js.native
@JSImport("react-router", "Router")
object NativeRouter extends ReactClass

@js.native
@JSImport("react-router", "Route")
object NativeRoute extends ReactClass

@js.native
@JSImport("react-router", "IndexRoute")
object NativeIndexRoute extends ReactClass

@js.native
@JSImport("react-router", "Link")
object NativeLink extends ReactClass

/** Facade for react-router */
object Router {
  /** [[VirtualDOM]] extension for router components
    *
    * {{{
    * import io.github.shogowada.scalajs.reactjs.VirtualDOM._
    * import io.github.shogowada.scalajs.reactjs.router.Router._
    *
    * <.Router(history = HashHistory)(
    *   <.Route(path = "/", component = new App())(
    *     <.Route(path = "about", component = new About())(),
    *     <.Route(path = "repos", component = new Repos())(
    *       <.Route(path = ":id", component = new Repo())()
    *     )
    *   )
    * )
    * }}}
    * */
  implicit class RichVirtualDOMElements(elements: VirtualDOMElements) {
    def Router(history: History)(routes: ReactElement*): ReactElement = {
      val props = js.Dynamic.literal(
        "history" -> history
      )
      React.createElement(NativeRouter, props, routes: _*)
    }

    def Route[Props, State](path: String, component: ReactClassSpec[Props, State])(childRoutes: ReactElement*): ReactElement = {
      val realComponent = React.createClass(component)
      Route(path = path, component = realComponent)(childRoutes: _*)
    }

    def Route(path: String, component: ReactClass)(childRoutes: ReactElement*): ReactElement = {
      val props = js.Dynamic.literal(
        "path" -> path,
        "component" -> component
      )
      React.createElement(NativeRoute, props, childRoutes: _*)
    }

    def IndexRoute[Props, State](component: ReactClassSpec[Props, State]): ReactElement = {
      IndexRoute(React.createClass(component))
    }

    def IndexRoute(component: ReactClass): ReactElement = {
      val props = js.Dynamic.literal(
        "component" -> component
      )
      React.createElement(NativeIndexRoute, props)
    }

    def Link(to: String)(contents: Any*): ReactElement = {
      val props = js.Dynamic.literal(
        "to" -> to
      )
      React.createElement(NativeLink, props, VirtualDOM.elementsToReactElements(contents): _*)
    }
  }
}

@js.native
trait Location extends js.Object {
  val pathname: String = js.native
  val search: String = js.native
  val state: js.UndefOr[js.Object] = js.native
  val action: String = js.native
  val key: js.UndefOr[String] = js.native
}

@js.native
trait NativeRouter extends js.Object {
  def push(path: String): Unit = js.native
  def replace(path: String): Unit = js.native
  def go(delta: Int): Unit = js.native
  def goBack(): Unit = js.native
  def goForward(): Unit = js.native

  def setRouteLeaveHook(route: ReactElement, hook: js.Function1[Location, Boolean | String]): js.Function0[js.Any] = js.native

  def isActive(path: String): Boolean = js.native
}

class Router(native: NativeRouter) {
  def push(path: String): Unit = native.push(path)
  def replace(path: String): Unit = native.replace(path)
  def go(delta: Int): Unit = native.go(delta)
  def goBack(): Unit = native.goBack()
  def goForward(): Unit = native.goForward()

  def setRouteLeaveHook(route: ReactElement, hook: Location => Boolean | String): () => Unit = {
    val unset = native.setRouteLeaveHook(route, hook)
    () => unset()
  }

  def isActive(path: String): Boolean = native.isActive(path)
}
