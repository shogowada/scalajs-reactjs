package io.github.shogowada.scalajs.reactjs.example.interactive.helloworld

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.specs.{PropslessReactClassSpec, StatelessReactClassSpec}
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.{InputFormSyntheticEvent, RadioFormSyntheticEvent}
import io.github.shogowada.scalajs.reactjs.example.interactive.helloworld.LetterCase.{DEFAULT, LOWER_CASE, LetterCase, UPPER_CASE}
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

  def apply() = new LetterCaseRadioBox()
}

class LetterCaseRadioBox extends StatelessReactClassSpec[LetterCaseRadioBox.WrappedProps] {
  override def render(): ReactElement = {
    <.span()(
      <.input(
        ^.`type`.radio,
        ^.name := "letter-case",
        ^.value := props.wrapped.letterCase.name,
        ^.checked := props.wrapped.checked,
        ^.onChange := onChange
      )(),
      props.wrapped.letterCase.name
    )
  }

  val onChange = (event: RadioFormSyntheticEvent) => {
    if (event.target.checked) {
      props.wrapped.onChecked()
    }
  }
}

object InteractiveHelloWorld {
  case class State(name: String, letterCase: LetterCase)
}

class InteractiveHelloWorld extends PropslessReactClassSpec[InteractiveHelloWorld.State] {

  import InteractiveHelloWorld._

  override def getInitialState(): State = State(
    name = "whoever you are",
    letterCase = DEFAULT
  )

  val nameId = "name"

  override def render() =
    <.div()(
      createNameInput(),
      LetterCase.ALL.map(createLetterCaseRadioBox),
      <.br.empty,
      <.div(^.id := "greet")(s"Hello, ${name(state)}!")
    )

  def createNameInput() =
    <.div()(
      <.label(^.`for` := nameId)("Name: "),
      <.input(
        ^.id := nameId,
        ^.value := state.name,
        ^.onChange := onChange
      )()
    )

  def createLetterCaseRadioBox(thisLetterCase: LetterCase): ReactElement = {
    <(LetterCaseRadioBox())(
      ^.wrapped := LetterCaseRadioBox.WrappedProps(
        letterCase = thisLetterCase,
        checked = thisLetterCase == state.letterCase,
        onChecked = () => {
          setState(_.copy(letterCase = thisLetterCase))
        }
      )
    )()
  }

  val onChange = (event: InputFormSyntheticEvent) => {
    val name = event.target.value
    setState(_.copy(name = name))
  }

  def name(state: State): String = {
    state.letterCase match {
      case LOWER_CASE => state.name.toLowerCase
      case UPPER_CASE => state.name.toUpperCase
      case _ => state.name
    }
  }
}

object Main extends JSApp {
  def main(): Unit = {
    val mountNode = dom.document.getElementById("mount-node")
    ReactDOM.render(<(new InteractiveHelloWorld()).empty, mountNode)
  }
}
