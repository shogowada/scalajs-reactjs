package io.github.shogowada.scalajs.reactjs.example.helloworld

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.specs.StatelessReactClassSpec
import org.scalajs.dom

import scala.scalajs.js.JSApp

object Main extends JSApp {
  def main(): Unit = {
    class HelloWorld extends StatelessReactClassSpec {

      override type Props = HelloWorld.Props

      override def render() = <.div(^.id := "hello-world")(s"Hello, ${props.name}!")
    }

    object HelloWorld {

      case class Props(name: String)

    }
    val mountNode = dom.document.getElementById("mount-node")
    ReactDOM.render(new HelloWorld()(HelloWorld.Props("World")), mountNode)
  }
}
