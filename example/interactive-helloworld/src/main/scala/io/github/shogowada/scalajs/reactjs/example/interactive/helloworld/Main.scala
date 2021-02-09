package io.github.shogowada.scalajs.reactjs.example.interactive.helloworld

import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.FormSyntheticEvent
import io.github.shogowada.scalajs.reactjs.example.interactive.helloworld.LetterCase.{DEFAULT, LOWER_CASE, LetterCase, UPPER_CASE}
import io.github.shogowada.scalajs.reactjs.{React, ReactDOM}
import org.scalajs.dom
import org.scalajs.dom.raw.HTMLInputElement

import scala.scalajs.js.annotation.JSExport

object Main {

  @JSExport
  def main(args: Array[String]): Unit = {
    val mountNode = dom.document.getElementById("mount-node")
    ReactDOM.render(<(InteractiveHelloWorld()).empty, mountNode)
  }
}

object InteractiveHelloWorld {
  case class State(name: String, letterCase: LetterCase)

  type Self = React.Self[Unit, State]

  private val nameId = "name"

  def apply() = reactClass

  private lazy val reactClass = React.createClass[Unit, State](
    getInitialState = (self) => State(
      name = "whoever you are",
      letterCase = DEFAULT
    ),
    render = (self) =>
      <.div()(
        createNameInput(self),
        LetterCase.ALL.map(createLetterCaseRadioBox(self, _)),
        <.br.empty,
        <.div(^.id := "greet")(s"Hello, ${name(self.state)}!")
      )
  )

  private def createNameInput(self: Self) =
    <.div()(
      <.label(^.`for` := nameId)("Name: "),
      <.input(
        ^.id := nameId,
        ^.value := self.state.name,
        ^.onChange := onChange(self)
      )()
    )

  private def createLetterCaseRadioBox(self: Self, thisLetterCase: LetterCase): ReactElement = {
    <(LetterCaseRadioBox())(
      ^.wrapped := LetterCaseRadioBox.WrappedProps(
        letterCase = thisLetterCase,
        checked = thisLetterCase == self.state.letterCase,
        onChecked = () => {
          self.setState(_.copy(letterCase = thisLetterCase))
        }
      )
    )()
  }

  private def onChange(self: Self) =
    (event: FormSyntheticEvent[HTMLInputElement]) => {
      val name = event.target.value
      self.setState(_.copy(name = name))
    }

  private def name(state: State): String =
    state.letterCase match {
      case LOWER_CASE => state.name.toLowerCase
      case UPPER_CASE => state.name.toUpperCase
      case _ => state.name
    }
}

object LetterCase {
  sealed class LetterCase(val name: String)

  case object DEFAULT extends LetterCase("Default")
  case object LOWER_CASE extends LetterCase("Lower Case")
  case object UPPER_CASE extends LetterCase("Upper Case")

  val ALL = Seq(DEFAULT, LOWER_CASE, UPPER_CASE)
}

object LetterCaseRadioBox {
  case class WrappedProps(letterCase: LetterCase, checked: Boolean, onChecked: () => Unit)

  type Self = React.Self[WrappedProps, Unit]

  def apply() = reactClass

  private lazy val reactClass = React.createClass[WrappedProps, Unit](
    (self) =>
      <.span()(
        <.input(
          ^.`type`.radio,
          ^.name := "letter-case",
          ^.value := self.props.wrapped.letterCase.name,
          ^.checked := self.props.wrapped.checked,
          ^.onChange := onChange(self)
        )(),
        self.props.wrapped.letterCase.name
      )
  )

  private def onChange(self: Self) =
    (event: FormSyntheticEvent[HTMLInputElement]) => {
      if (event.target.checked) {
        self.props.wrapped.onChecked()
      }
    }
}
