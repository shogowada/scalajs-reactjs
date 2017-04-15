package io.github.shogowada.scalajs.reactjs.example.helloworld

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.specs.StatelessReactClassSpec
import org.scalajs.dom

import scala.scalajs.js.JSApp

object Main extends JSApp {
  def main(): Unit = {
    class HelloWorld extends StatelessReactClassSpec[HelloWorld.WrappedProps] {
      override def render() = <.div(^.id := "hello-world")(s"Hello, ${props.wrapped.name}!")
    }

    object HelloWorld {
      case class WrappedProps(name: String)

      def apply() = new HelloWorld()
    }

    val mountNode = dom.document.getElementById("mount-node")
    ReactDOM.render(<(HelloWorld())(^.wrapped := HelloWorld.WrappedProps("World"))(), mountNode)
  }
}
