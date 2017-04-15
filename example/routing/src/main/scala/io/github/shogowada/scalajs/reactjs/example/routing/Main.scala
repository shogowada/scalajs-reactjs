package io.github.shogowada.scalajs.reactjs.example.routing

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.specs.{Props, PropslessReactClassSpec, StaticReactClassSpec}
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.CheckBoxFormSyntheticEvent
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import io.github.shogowada.scalajs.reactjs.router.{RouterProps, WithRouter}
import org.scalajs.dom

import scala.scalajs.js.JSApp

/*
 * If you are not yet familiar with react-router, check it our first:
 *
 * - https://reacttraining.com/react-router/web/guides/quick-start
 *
 * This is just a facade for the react-router, so if you know how to use it already,
 * you will be able to more easily understand how to use this facade.
 *
 * Import the following to access router components (e.g. Route):
 *
 * - import io.github.shogowada.scalajs.reactjs.VirtualDOM._
 * - import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
 * */

object Main extends JSApp {
  override def main(): Unit = {
    val mountNode = dom.document.getElementById("mount-node")
    ReactDOM.render(
      <.HashRouter()(
        <(App()).empty
      ),
      mountNode
    )
  }
}

object App {
  def apply() = WithRouter(new App())
}

class App extends StaticReactClassSpec {
  override def render() =
    <.div()(
      <.h1()("React Router Tutorial"),
      Links(),
      RouterApiButtons(props),
      <.Switch()(
        <.Route(^.path := "/about", ^.render := (About(_: Props[_])))(),
        <.Route(^.path := "/repos", ^.render := (Repos(_: Props[_])))(),
        <.Route(^.path := "/form", ^.component := Form())()
      )
    ).asReactElement
}

object Links {
  def apply(): ReactElement = <.nav()(
    <.li()(<.Link(^.to := "/about")("About")),
    <.li()(<.Link(^.to := "/repos")("Repos"))
  )
}

// Extend RouterProps or import RouterProps._ to access router specific props like props.match or props.history.
object RouterApiButtons extends RouterProps {
  def apply(props: Props[_]): ReactElement = <.div()(
    <.button(
      ^.id := "push-about",
      ^.onClick := (() => {
        props.history.push("/about")
      })
    )("Push /about"),
    <.button(
      ^.id := "go-back",
      ^.onClick := (() => {
        props.history.goBack()
      })
    )("Go back"),
    <.button(
      ^.id := "go-forward",
      ^.onClick := (() => {
        props.history.goForward()
      })
    )("Go forward")
  )
}

object About {
  def apply(props: Props[_]): ReactElement =
    <.div(^.id := "about")("About")
}

object Repos extends RouterProps {
  def apply(props: Props[_]): ReactElement =
    <.div(^.id := "repos")(
      "Repos",
      <.Route(
        ^.path := s"${props.`match`.path}/:id",
        ^.component := Repo()
      )()
    )
}

object Repo {
  def apply() = new Repo()
}

class Repo extends StaticReactClassSpec
    with RouterProps {
  // Params has type of js.Dictionary[String].
  private def id: String = props.`match`.params("id")

  override def render() = <.div(^.id := s"repo-${id}")(s"Repo ${id}")
}

object Form {
  case class State(confirmBeforeLeave: Boolean)

  def apply() = new Form()
}

class Form extends PropslessReactClassSpec[Form.State] {

  import Form._

  override def getInitialState() = State(confirmBeforeLeave = true)

  override def render(): ReactElement =
    <.div()(
      <.Prompt(
        ^.when := state.confirmBeforeLeave,
        ^.message := "Are you sure you want to leave the page?"
      )(),
      <.div(^.id := "form")(
        <.label()(
          "Confirm before leave",
          <.input(
            ^.id := "confirm-before-leave",
            ^.`type`.checkbox,
            ^.checked := state.confirmBeforeLeave,
            ^.onChange := ((event: CheckBoxFormSyntheticEvent) => {
              val checked = event.target.checked
              setState(State(confirmBeforeLeave = checked))
            })
          )()
        )
      )
    )
}
