package io.github.shogowada.scalajs.reactjs.example.helloworld

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.specs.StatelessReactClassSpec
import org.scalajs.dom.raw.HTMLElement

import scala.scalajs.js.annotation.JSExport

@JSExport
object Main {
  @JSExport
  def main(element: HTMLElement) = {
    class HelloWorldSpec extends StatelessReactClassSpec {

      case class Props(name: String)

      override def render() = <.div(^.id := "hello-world")(s"Hello, ${props.name}!")
    }

    val spec = new HelloWorldSpec()
    ReactDOM.render(spec, spec.Props("World"), element)
  }
}
