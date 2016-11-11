package io.github.shogowada.scalajs.reactjs.router

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.classes.specs.{ReactClassSpec, StatelessReactClassSpec}
import io.github.shogowada.scalajs.reactjs.elements.ReactElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
trait History extends js.Object

@js.native
@JSImport("react-router", "browserHistory")
object BrowserHistory extends History

@js.native
@JSImport("react-router", "hashHistory")
object HashHistory extends History

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
trait RoutedComponentProps extends js.Object {
  def params: js.Dynamic = js.native

  def children: Seq[js.Object] = js.native
}

object Router {

  implicit class RichVirtualDOMElements(elements: VirtualDOMElements) {
    def Router(history: History)(routes: ReactElement*): ReactElement = {
      val props = js.Dynamic.literal(
        "history" -> history
      )
      React.createElement(NativeRouter, props, routes)
    }

    def Route(path: String, component: ReactClassSpec)(childRoutes: ReactElement*): ReactElement = {
      val realComponent = React.createClass(component)
      Route(path = path, component = realComponent)(childRoutes: _*)
    }

    def Route(path: String, component: ReactClass)(childRoutes: ReactElement*): ReactElement = {
      val props = js.Dynamic.literal(
        "path" -> path,
        "component" -> component
      )
      React.createElement(NativeRoute, props, childRoutes)
    }

    def IndexRoute(component: ReactClass): ReactElement = {
      val props = js.Dynamic.literal(
        "component" -> component
      )
      React.createElement(NativeIndexRoute, props)
    }
  }

}
