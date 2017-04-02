package io.github.shogowada.scalajs.reactjs.example.routing

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.specs.{PropslessReactClassSpec, StaticReactClassSpec}
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.Router._
import io.github.shogowada.scalajs.reactjs.router.{HashHistory, RoutedReactClassSpec}
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.JSApp

object Links {
  def apply(): ReactElement = <.nav()(
    <.li()(<.Link(to = "about")("About")),
    <.li()(<.Link(to = "repos")("Repos"))
  )
}

object HistoryApiButtons {
  case class State(unblock: Option[js.Function0[js.Any]])

  def apply(): ReactElement = (new HistoryApiButtons()) ()
}

class HistoryApiButtons extends PropslessReactClassSpec[HistoryApiButtons.State] {

  import HistoryApiButtons._

  override def getInitialState() = State(None)

  override def render(): ReactElement = <.div()(
    <.button(
      ^.id := "push-about",
      ^.onClick := (() => {
        HashHistory.push("/about")
      })
    )("Push /about"),
    <.button(
      ^.id := "go-back",
      ^.onClick := (() => {
        HashHistory.goBack()
      })
    )("Go back"),
    <.button(
      ^.id := "go-forward",
      ^.onClick := (() => {
        HashHistory.goForward()
      })
    )("Go forward")
  )
}

class App extends StaticReactClassSpec {
  override def render() =
    <.div()(
      <.h1()("React Router Tutorial"),
      Links(),
      HistoryApiButtons(),
      children
    ).asReactElement
}

class About extends StaticReactClassSpec {
  override def render() = <.div(^.id := "about")("About")
}

class Repos extends StaticReactClassSpec {
  override def render() = <.div(^.id := "repos")(
    "Repos",
    children
  )
}

object Repo {
  @js.native
  trait Params extends js.Object {
    val id: String = js.native
  }
}

class Repo extends StaticReactClassSpec
    with RoutedReactClassSpec[Repo.Params] {
  override def render() = <.div(^.id := s"repo-${params.id}")(s"Repo ${params.id}")
}

/*
* To access router components, import the following:
*
* - import io.github.shogowada.scalajs.reactjs.VirtualDOM._
* - import io.github.shogowada.scalajs.reactjs.router.Router._
*
* */
object Index {
  def apply() = {
    <.Router(history = HashHistory)(
      <.Route(path = "/", component = new App())(
        <.Route(path = "about", component = new About())(),
        <.Route(path = "repos", component = new Repos())(
          <.Route(path = ":id", component = new Repo())()
        )
      )
    )
  }
}

object Main extends JSApp {
  override def main(): Unit = {
    val mountNode = dom.document.getElementById("mount-node")
    ReactDOM.render(Index(), mountNode)
  }
}
