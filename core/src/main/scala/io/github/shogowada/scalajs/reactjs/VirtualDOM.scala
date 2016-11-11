package io.github.shogowada.scalajs.reactjs

import io.github.shogowada.scalajs.reactjs.classes.specs.ReactClassSpec
import io.github.shogowada.scalajs.reactjs.elements.{ReactElement, ReactHTMLElement}
import io.github.shogowada.scalajs.reactjs.events.SyntheticEvent
import io.github.shogowada.statictags._

import scala.language.implicitConversions
import scala.scalajs.js
import scala.scalajs.js.JSConverters._

trait VirtualDOM extends StaticTags {

  class VirtualDOMElements extends Elements

  class VirtualDOMAttributes extends Attributes {

    case class OnEventAttribute(name: String) extends AttributeSpec {
      def :=(callback: js.Function0[_]) = {
        Attribute[js.Function0[_]](name = name, value = callback)
      }

      def :=[EVENT <: SyntheticEvent](callback: js.Function1[EVENT, _]) = {
        Attribute[js.Function1[EVENT, _]](name = name, value = callback)
      }
    }

    case class RefAttributeSpec(name: String) extends AttributeSpec {
      def :=[T <: ReactHTMLElement](callback: js.Function1[T, Unit]) = {
        Attribute[js.Function1[T, Unit]](name = name, value = callback)
      }
    }

    lazy val className = SpaceSeparatedStringAttributeSpec(name = "className")
    override lazy val `for` = htmlFor
    lazy val htmlFor = ForAttributeSpec("htmlFor")
    lazy val key = StringAttributeSpec("key")
    lazy val onChange = OnEventAttribute("onChange")
    lazy val onSubmit = OnEventAttribute("onSubmit")
    lazy val ref = RefAttributeSpec("ref")
  }

  override val < = new VirtualDOMElements()
  override val ^ = new VirtualDOMAttributes()

  implicit def elementsToVirtualDoms(element: Element): ReactElement = {
    React.createElement(
      element.name,
      attributesToReactAttributes(element.flattenedAttributes),
      elementsToReactElements(element.flattenedContents): _*
    )
  }

  private def attributesToReactAttributes(attributes: Iterable[Attribute[_]]): js.Dictionary[Any] = {
    attributes.map(attribute => (VirtualDOMAttributeNameFactory(attribute.name), attribute.value))
        .toMap
        .toJSDictionary
  }

  def elementsToReactElements(contents: Seq[Any]): Seq[js.Any] = {
    contents.map(elementToReactElement)
  }

  private def elementToReactElement(content: Any): js.Any = {
    content match {
      case element@Element(_, _, _, _) => elementsToVirtualDoms(element)
      case spec: ReactClassSpec => React.createElement(spec)
      case _ => content.asInstanceOf[js.Any]
    }
  }
}

object VirtualDOM extends VirtualDOM

object VirtualDOMAttributeNameFactory {
  lazy val nameToReactNameMap = Map(
    "accept-charset" -> "acceptCharset",
    "accesskey" -> "accessKey",
    "autocomplete" -> "autoComplete",
    "autofocus" -> "autoFocus",
    "autoplay" -> "autoPlay",
    "charset" -> "charSet",
    "colspan" -> "colSpan",
    "contenteditable" -> "contentEditable",
    "crossorigin" -> "crossOrigin",
    "datetime" -> "dateTime",
    "enctype" -> "encType",
    "formaction" -> "formAction",
    "formenctype" -> "formEncType",
    "formmethod" -> "formMethod",
    "formnovalidate" -> "formNoValidate",
    "formtarget" -> "formTarget",
    "hreflang" -> "hrefLang",
    "http-equiv" -> "httpEquiv",
    "keytype" -> "keyType",
    "maxlength" -> "maxLength",
    "mediagroup" -> "mediaGroup",
    "minlength" -> "minLength",
    "novalidate" -> "noValidate",
    "spellcheck" -> "spellCheck",
    "srcdoc" -> "srcDoc",
    "srclang" -> "srcLang",
    "tabindex" -> "tabIndex",
    "usemap" -> "useMap"
  )

  def apply(name: String): String = {
    nameToReactNameMap.getOrElse(name, name)
  }
}
