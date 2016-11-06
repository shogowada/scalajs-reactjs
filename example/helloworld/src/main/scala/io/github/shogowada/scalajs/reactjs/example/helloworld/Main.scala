package io.github.shogowada.scalajs.reactjs.example.helloworld

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.specs.StatelessReactClassSpec
import org.scalajs.dom.raw.HTMLElement

import scala.scalajs.js.annotation.JSExport

@JSExport
object Main {
  @JSExport
  def main(mountNode: HTMLElement) = {
    case class HelloWorldProps(name: String)

    class HelloWorld extends StatelessReactClassSpec {

      override type Props = HelloWorld.Props

      override def render() = <.div(^.id := "hello-world")(s"Hello, ${props.name}!")
    }

    object HelloWorld {

      case class Props(name: String)

    }

    ReactDOM.render(new HelloWorld()(HelloWorld.Props("World")), mountNode)
  }
}
