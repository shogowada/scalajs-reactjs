package io.github.shogowada.scalajs.reactjs.router

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.classes.specs.ReactClassSpec
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.{React, VirtualDOM}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

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
