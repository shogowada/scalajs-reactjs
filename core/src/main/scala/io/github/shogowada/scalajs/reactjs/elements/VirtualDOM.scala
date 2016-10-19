package io.github.shogowada.scalajs.reactjs.elements

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.statictags.StaticTags.{Attributes, Elements}
import io.github.shogowada.statictags.{Attribute, AttributeSpec, Element}

import scala.language.implicitConversions
import scala.scalajs.js

object VirtualDOM {

  case class OnChangeAttributeSpec(name: String) extends AttributeSpec {
    def :=(callback: js.Function0[Unit]): Attribute[js.Function0[Unit]] = {
      Attribute(name = name, value = callback)
    }
  }

  class VirtualDOMAttributes extends Attributes {
    lazy val onChange = new OnChangeAttributeSpec("onChange")
  }

  val < = new Elements()
  val ^ = new VirtualDOMAttributes()

  implicit def asReactElement(element: Element): ReactElement = {
    React.createElement(element.name, null, toReactContents(element.contents): _*)
  }

  private def toReactContents(contents: Seq[Any]): Seq[js.Any] = {
    contents.map(toReactContent)
  }

  private def toReactContent(content: Any): js.Any = {
    content match {
      case element@Element(_, _, _) => asReactElement(element)
      case _ => content.toString
    }
  }
}
