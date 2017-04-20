package io.github.shogowada.scalajs.reactjs.example.helloworldfunction

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import org.scalajs.dom

import scala.scalajs.js.JSApp

object Main extends JSApp {
  def main(): Unit = {
    object HelloWorld {
      def apply(name: String) = <.div(^.id := "hello-world")(s"Hello, ${name}!")
    }

    val mountNode = dom.document.getElementById("mount-node")
    ReactDOM.render(HelloWorld("World"), mountNode)
  }
}
