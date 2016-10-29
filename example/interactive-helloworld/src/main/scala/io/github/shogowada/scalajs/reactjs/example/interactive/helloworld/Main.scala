package io.github.shogowada.scalajs.reactjs.example.interactive.helloworld

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.specs.ReactClassSpec
import io.github.shogowada.scalajs.reactjs.elements.{ReactHTMLInputElement, ReactHTMLRadioElement}
import org.scalajs.dom.raw.HTMLElement

import scala.scalajs.js.annotation.JSExport

@JSExport
object Main {

  @JSExport
  def main(element: HTMLElement): Unit = {
    object InteractiveHelloWorld extends ReactClassSpec {

      sealed class LetterCase(val name: String)

      case object Default extends LetterCase("Default")

      case object LowerCase extends LetterCase("Lower Case")

      case object UpperCase extends LetterCase("Upper Case")

      val letterCases = Seq(Default, LowerCase, UpperCase)

      case class State(name: String, letterCase: LetterCase)

      override def getInitialState(): State = State(
        name = "whoever you are",
        letterCase = Default
      )

      val nameId = "name"
      var nameElement: ReactHTMLInputElement = _
      var letterCaseElements: Map[LetterCase, ReactHTMLRadioElement] = Map()

      override def render() = {
        <.div()(
          createNameInput(),
          <.br.empty,
          createLetterCaseRadioBoxes(),
          <.br.empty,
          <.div(^.id := "greet")(s"Hello, ${name(state)}!")
        )
      }

      def createNameInput(): Seq[_] = {
        Seq(
          <.label(^.`for` := nameId)("Name:"),
          <.input(
            ^.id := nameId,
            ^.ref := ((element: ReactHTMLInputElement) => {
              nameElement = element
            }),
            ^.value := state.name,
            ^.onChange := onChange
          )()
        )
      }

      def createLetterCaseRadioBoxes(): Seq[_] = {
        letterCases.flatMap(createLetterCaseRadioBox)
      }

      def createLetterCaseRadioBox(letterCase: LetterCase): Seq[_] = {
        Seq(
          <.input(
            ^.`type` := "radio",
            ^.name := "letter-case",
            ^.value := letterCase.name,
            ^.ref := ((element: ReactHTMLRadioElement) => {
              letterCaseElements = letterCaseElements + (letterCase -> element)
            }),
            ^.checked := state.letterCase == letterCase,
            ^.onChange := onChange
          )(),
          letterCase.name
        )
      }

      val onChange = () => {
        setState(State(
          name = nameElement.value,
          letterCase = letterCaseElements
              .find { case (_, value) => value.checked }
              .map { case (key, _) => key }
              .getOrElse(Default)
        ))
      }

      def name(state: State): String = {
        state.letterCase match {
          case LowerCase => state.name.toLowerCase
          case UpperCase => state.name.toUpperCase
          case _ => state.name
        }
      }
    }

    ReactDOM.render(InteractiveHelloWorld, element)
  }
}
