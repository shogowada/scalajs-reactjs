package io.github.shogowada.scalajs.reactjs.router

import io.github.shogowada.scalajs.reactjs.VirtualDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMAttributes.Type.AS_IS
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.classes.specs.ReactClassSpec
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.statictags.{Attribute, AttributeSpec, StringAttributeSpec}

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
    * <.Router(^.history := HashHistory)(
    *   <.Route(^.path := "/", ^.component := new App())(
    *     <.Route(^.path := "about", ^.component := new About())(),
    *     <.Route(^.path := "repos", ^.component := new Repos())(
    *       <.Route(^.path := ":id", ^.component := new Repo())()
    *     )
    *   )
    * )
    * }}}
    * */
  implicit class RichVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Router = ReactClassElementSpec(NativeRouter)
    lazy val Route = ReactClassElementSpec(NativeRoute)
    lazy val IndexRoute = ReactClassElementSpec(NativeIndexRoute)
    lazy val Link = ReactClassElementSpec(NativeLink)
  }

  implicit class RichVirtualDOMAttributes(attribute: VirtualDOMAttributes) {
    case class ReactClassAttributeSpec(name: String) extends AttributeSpec {
      def :=[Props, State](value: ReactClassSpec[Props, State]): Attribute[ReactClassSpec[Props, State]] =
        Attribute(name, value, AS_IS)
      def :=(value: ReactClass): Attribute[ReactClass] = Attribute(name, value, AS_IS)
    }

    case class HistoryAttributeSpec(name: String) extends AttributeSpec {
      def :=(value: History): Attribute[History] = Attribute(name, value, AS_IS)
    }

    lazy val component = ReactClassAttributeSpec("component")
    lazy val history = HistoryAttributeSpec("history")
    lazy val path = StringAttributeSpec("path")
    lazy val to = StringAttributeSpec("to")
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
