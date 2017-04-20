package io.github.shogowada.scalajs.reactjs.example.interactive.helloworld

import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.{InputFormSyntheticEvent, RadioFormSyntheticEvent}
import io.github.shogowada.scalajs.reactjs.example.interactive.helloworld.LetterCase.{DEFAULT, LOWER_CASE, LetterCase, UPPER_CASE}
import io.github.shogowada.scalajs.reactjs.{React, ReactDOM}
import org.scalajs.dom

import scala.scalajs.js.JSApp

object LetterCase {
  sealed class LetterCase(val name: String)

  case object DEFAULT extends LetterCase("Default")
  case object LOWER_CASE extends LetterCase("Lower Case")
  case object UPPER_CASE extends LetterCase("Upper Case")

  val ALL = Seq(DEFAULT, LOWER_CASE, UPPER_CASE)
}

object LetterCaseRadioBox {
  case class WrappedProps(letterCase: LetterCase, checked: Boolean, onChecked: () => Unit)

  type Context = React.Context[WrappedProps, Unit]

  def apply() = reactClass

  private lazy val reactClass = React.createClass[WrappedProps, Unit](
    render = (context) =>
      <.span()(
        <.input(
          ^.`type`.radio,
          ^.name := "letter-case",
          ^.value := context.props.wrapped.letterCase.name,
          ^.checked := context.props.wrapped.checked,
          ^.onChange := onChange(context)
        )(),
        context.props.wrapped.letterCase.name
      )
  )

  private def onChange(context: Context) =
    (event: RadioFormSyntheticEvent) => {
      if (event.target.checked) {
        context.props.wrapped.onChecked()
      }
    }
}

object InteractiveHelloWorld {
  case class State(name: String, letterCase: LetterCase)

  type Context = React.Context[Unit, State]

  private val nameId = "name"

  def apply() = reactClass

  private lazy val reactClass = React.createClass[Unit, State](
    getInitialState = (context) => State(
      name = "whoever you are",
      letterCase = DEFAULT
    ),

    render = (context) =>
      <.div()(
        createNameInput(context),
        LetterCase.ALL.map(createLetterCaseRadioBox(context, _)),
        <.br.empty,
        <.div(^.id := "greet")(s"Hello, ${name(context.state)}!")
      )
  )

  private def createNameInput(context: Context) =
    <.div()(
      <.label(^.`for` := nameId)("Name: "),
      <.input(
        ^.id := nameId,
        ^.value := context.state.name,
        ^.onChange := onChange(context)
      )()
    )

  private def createLetterCaseRadioBox(context: Context, thisLetterCase: LetterCase): ReactElement = {
    <(LetterCaseRadioBox())(
      ^.wrapped := LetterCaseRadioBox.WrappedProps(
        letterCase = thisLetterCase,
        checked = thisLetterCase == context.state.letterCase,
        onChecked = () => {
          context.setState(_.copy(letterCase = thisLetterCase))
        }
      )
    )()
  }

  private def onChange(context: Context) =
    (event: InputFormSyntheticEvent) => {
      val name = event.target.value
      context.setState(_.copy(name = name))
    }

  private def name(state: State): String =
    state.letterCase match {
      case LOWER_CASE => state.name.toLowerCase
      case UPPER_CASE => state.name.toUpperCase
      case _ => state.name
    }
}

object Main extends JSApp {
  def main(): Unit = {
    val mountNode = dom.document.getElementById("mount-node")
    ReactDOM.render(<(InteractiveHelloWorld()).empty, mountNode)
  }
}
