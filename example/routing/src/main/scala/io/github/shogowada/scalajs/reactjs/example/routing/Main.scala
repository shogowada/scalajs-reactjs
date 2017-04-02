package io.github.shogowada.scalajs.reactjs.example.routing

import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.specs.{PropslessReactClassSpec, StaticReactClassSpec}
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.CheckBoxFormSyntheticEvent
import io.github.shogowada.scalajs.reactjs.router.Router._
import io.github.shogowada.scalajs.reactjs.router.{HashHistory, Location, RoutedReactClassSpec, WithRouter}
import io.github.shogowada.scalajs.reactjs.{React, ReactDOM}
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.JSApp

object Links {
  def apply(): ReactElement = <.nav()(
    <.li()(<.Link(to = "about")("About")),
    <.li()(<.Link(to = "repos")("Repos"))
  )
}

object RouterApiButtons {
  /* Wrap with WithRouter to create a component with router API available */
  def apply(): ReactElement = React.createElement(WithRouter(new RouterApiButtons()))
}

/*
* Extend RoutedReactClassSpec to access router API.
* If you don't have params, just use js.Object as Params type parameter.
**/
class RouterApiButtons extends StaticReactClassSpec
    with RoutedReactClassSpec[js.Object] {

  override def render(): ReactElement = <.div()(
    <.button(
      ^.id := "push-about",
      ^.onClick := (() => {
        router.push("/about")
      })
    )("Push /about"),
    <.button(
      ^.id := "go-back",
      ^.onClick := (() => {
        router.goBack()
      })
    )("Go back"),
    <.button(
      ^.id := "go-forward",
      ^.onClick := (() => {
        router.goForward()
      })
    )("Go forward")
  )
}

class App extends StaticReactClassSpec {
  override def render() =
    <.div()(
      <.h1()("React Router Tutorial"),
      Links(),
      RouterApiButtons(),
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

object Form {
  case class State(confirmBeforeLeave: Boolean)
}

class Form extends PropslessReactClassSpec[Form.State]
    with RoutedReactClassSpec[js.Object] {

  import Form._

  private var unsetRouteLeaveHook: js.Function0[js.Any] = _

  override def getInitialState() = State(
    confirmBeforeLeave = true
  )

  override def componentDidMount(): Unit = {
    /* Confirm users if they want to leave */
    unsetRouteLeaveHook = router.setRouteLeaveHook(route, (nextLocation: Location) => {
      if (state.confirmBeforeLeave) {
        "Are you sure you want to leave the page?"
      } else {
        true
      }
    })
  }

  override def render(): ReactElement = <.div(^.id := "form")(
    <.label()(
      "Confirm before leave",
      <.input(
        ^.id := "confirm-before-leave",
        ^.`type` := "checkbox",
        ^.checked := state.confirmBeforeLeave,
        ^.onChange := ((event: CheckBoxFormSyntheticEvent) => {
          val checked = event.target.checked
          setState(State(confirmBeforeLeave = checked))
        })
      )()
    ),
    <.button(
      ^.id := "unset-route-leave-hook",
      ^.onClick := (() => {
        unsetRouteLeaveHook()
        unsetRouteLeaveHook = null
      })
    )("Unset route leave hook")
  )
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
        ),
        <.Route(path = "form", component = new Form())()
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
