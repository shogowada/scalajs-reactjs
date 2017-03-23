package io.github.shogowada.scalajs.reactjs.example.helloworldfunction

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.scalajs.dom

import scala.scalajs.js.JSApp

object Main extends JSApp {
  def main(): Unit = {
    object HelloWorld {

      case class Props(name: String)

      def apply(props: Props): ReactElement = <.div(^.id := "hello-world")(s"Hello, ${props.name}!")
    }

    val mountNode = dom.document.getElementById("mount-node")
    ReactDOM.render(HelloWorld(HelloWorld.Props("World")), mountNode)
  }
}
