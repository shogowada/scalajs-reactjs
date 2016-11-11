package io.github.shogowada.scalajs.reactjs.example.routing

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.specs.StatelessReactClassSpec
import io.github.shogowada.scalajs.reactjs.router.Router._
import io.github.shogowada.scalajs.reactjs.router.{BrowserHistory, RoutedReactClassSpec}
import org.scalajs.dom

import scala.scalajs.js.JSApp

class App extends RoutedReactClassSpec {
  override def render() = {
    <.div()(
      <.h1()("React Router Tutorial"),
      <.ul()()
    )
  }
}

class Index extends StatelessReactClassSpec {

  override def render() = {
    <.Router(history = BrowserHistory)(
    )
  }
}

object Main extends JSApp {
  override def main(): Unit = {
    val mountNode = dom.document.getElementById("mount-node")
    ReactDOM.render(new Index(), mountNode)
  }
}
