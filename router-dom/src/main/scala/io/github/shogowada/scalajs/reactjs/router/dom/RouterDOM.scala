package io.github.shogowada.scalajs.reactjs.router.dom

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMAttributes.Type.AS_IS
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router
import io.github.shogowada.scalajs.reactjs.router.{Location, Match}
import io.github.shogowada.statictags.{AttributeSpec, _}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("react-router-dom", "BrowserRouter")
object NativeBrowserRouter extends ReactClass

@js.native
@JSImport("react-router-dom", "HashRouter")
object NativeHashRouter extends ReactClass

@js.native
@JSImport("react-router-dom", "Link")
object NativeLink extends ReactClass

@js.native
@JSImport("react-router-dom", "NavLink")
object NativeNavLink extends ReactClass

trait RouterDOM extends router.Router {
  implicit class RouterDOMVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val BrowserRouter = ReactClassElementSpec(NativeBrowserRouter)
    lazy val HashRouter = ReactClassElementSpec(NativeHashRouter)
    lazy val Link = ReactClassElementSpec(NativeLink)
    lazy val NavLink = ReactClassElementSpec(NativeNavLink)
  }

  object RouterDOMVirtualDOMAttributes {
    case class GetUserConfirmationAttributeSpec(name: String) extends AttributeSpec {
      type Get = js.Function2[String, js.Function1[Boolean, _], _]
      def :=(get: Get): Attribute[Get] = Attribute(name, get, AS_IS)
    }

    case class HashTypeAttributeSpec(name: String) extends AttributeSpec {
      def :=(hashType: String) = Attribute(name, hashType)

      lazy val hashbang = this := ("hashbang")
      lazy val noslash = this := ("noslash")
      lazy val slash = this := ("slash")
    }

    case class IsActiveAttributeSpec(name: String) extends AttributeSpec {
      type IsActive = js.Function2[Match, Location, Boolean]
      def :=(isActive: IsActive): Attribute[IsActive] =
        Attribute(name, isActive, AS_IS)
    }
  }

  implicit class RouterDOMVirtualDOMAttributes(attributes: VirtualDOMAttributes) {

    import RouterDOMVirtualDOMAttributes._

    lazy val activeClassName = SpaceSeparatedStringAttributeSpec("activeClassName")
    lazy val activeStyle = CssAttributeSpec("activeStyle")
    lazy val basename = StringAttributeSpec("basename")
    lazy val forceRefresh = BooleanAttributeSpec("forceRefresh")
    lazy val getUserConfirmation = GetUserConfirmationAttributeSpec("getUserConfirmation")
    lazy val hashType = HashTypeAttributeSpec("hashType")
    lazy val isActive = IsActiveAttributeSpec("isActive")
    lazy val keyLength = IntegerAttributeSpec("keyLength")
    lazy val replace = BooleanAttributeSpec("replace")
  }
}

object RouterDOM extends RouterDOM
