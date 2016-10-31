package io.github.shogowada.scalajs.reactjs.example.helloworld

import io.github.shogowada.scalajs.reactjs.classes.specs.StatelessReactClassSpec
import io.github.shogowada.scalajs.reactjs.{ReactDOM, VirtualDOM}
import org.scalajs.dom.raw.HTMLElement

import scala.scalajs.js.annotation.JSExport

@JSExport
object Main {
  @JSExport
  def main(element: HTMLElement) = {
    case class HelloWorldProps(name: String)

    class HelloWorldSpec extends StatelessReactClassSpec
        with VirtualDOM {

      override type Props = HelloWorldProps

      override def render() = <.div(^.id := "hello-world")(s"Hello, ${props.name}!")
    }

    ReactDOM.render(new HelloWorldSpec(), HelloWorldProps("World"), element)
  }
}
