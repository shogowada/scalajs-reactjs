package io.github.shogowada.scalajs.reactjs.example.routing

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.specs.StatelessReactClassSpec
import io.github.shogowada.scalajs.reactjs.router.Router._
import io.github.shogowada.scalajs.reactjs.router.{HashHistory, StatelessRoutedReactClassSpec}
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.JSApp

class App extends StatelessRoutedReactClassSpec {
  override def render() = {
    <.div()(
      <.h1()("React Router Tutorial"),
      <.nav()(
        <.li()(<.Link(to = "about")("About")),
        <.li()(<.Link(to = "repos")("Repos"))
      ),
      props.children
    )
  }
}

class About extends StatelessRoutedReactClassSpec {
  override def render() = <.div(^.id := "about")("About")
}

class Repos extends StatelessRoutedReactClassSpec {
  override def render() = <.div(^.id := "repos")("Repos")
}

@js.native
trait RepoParams extends js.Object {

  val id: String = js.native
}

class Repo extends StatelessRoutedReactClassSpec {

  override type Params = RepoParams

  override def render() = <.div(^.id := s"repo-${props.params.id}")(s"Repo ${props.params.id}")
}

class Index extends StatelessReactClassSpec {
  override def render() = {
    <.Router(history = HashHistory)(
      <.Route(path = "/", component = new App())(
        <.Route(path = "/about", component = new About())(),
        <.Route(path = "/repos", component = new Repos())(),
        <.Route(path = "/repos/:id", component = new Repo())()
      )
    )
  }
}

object Main extends JSApp {
  override def main(): Unit = {
    val mountNode = dom.document.getElementById("mount-node")
    ReactDOM.render(new Index(), mountNode)
  }
}
