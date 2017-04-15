package io.github.shogowada.scalajs.reactjs.router

import io.github.shogowada.scalajs.reactjs.VirtualDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMAttributes.Type.AS_IS
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMAttributes.{ReactClassAttributeSpec, RenderAttributeSpec}
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.statictags.{Attribute, AttributeSpec, BooleanAttributeSpec, StringAttributeSpec}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("react-router", "Prompt")
object NativePrompt extends ReactClass

@js.native
@JSImport("react-router", "Redirect")
object NativeRedirect extends ReactClass

@js.native
@JSImport("react-router", "Route")
object NativeRoute extends ReactClass

@js.native
@JSImport("react-router", "Router")
object NativeRouter extends ReactClass

@js.native
@JSImport("react-router", "Switch")
object NativeSwitch extends ReactClass

/** Facade for react-router */
trait Router {
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
  implicit class RouterVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Prompt = ReactClassElementSpec(NativePrompt)
    lazy val Redirect = ReactClassElementSpec(NativeRedirect)
    lazy val Route = ReactClassElementSpec(NativeRoute)
    lazy val Router = ReactClassElementSpec(NativeRouter)
    lazy val Switch = ReactClassElementSpec(NativeSwitch)
  }

  object RouterVirtualDOMAttributes {
    case class MessageAttributeSpec(name: String) extends AttributeSpec {
      def :=(message: String) = Attribute[String](name, message)
      def :=(message: js.Function0[String]) = Attribute[js.Function0[String]](name, message, AS_IS)
    }

    case class HistoryAttributeSpec(name: String) extends AttributeSpec {
      def :=(value: History): Attribute[History] = Attribute(name, value, AS_IS)
    }

    case class LocationAttributeSpec(name: String) extends AttributeSpec {
      def :=(path: String) = Attribute[String](name, path)
      def :=(location: Location) = Attribute[Location](name, location, AS_IS)
    }
  }

  implicit class RouterVirtualDOMAttributes(attribute: VirtualDOMAttributes) {

    import RouterVirtualDOMAttributes._

    lazy val children = RenderAttributeSpec("children")
    lazy val component = ReactClassAttributeSpec("component")
    lazy val exact = BooleanAttributeSpec("exact")
    lazy val from = StringAttributeSpec("from")
    lazy val history = HistoryAttributeSpec("history")
    lazy val message = MessageAttributeSpec("message")
    lazy val path = StringAttributeSpec("path")
    lazy val push = BooleanAttributeSpec("push")
    lazy val render = RenderAttributeSpec("render")
    lazy val strict = BooleanAttributeSpec("strict")
    lazy val to = LocationAttributeSpec("to")
    lazy val when = BooleanAttributeSpec("when")
  }
}

object Router extends Router
