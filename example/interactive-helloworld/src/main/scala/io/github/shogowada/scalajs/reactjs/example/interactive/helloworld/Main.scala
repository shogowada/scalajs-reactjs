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

      sealed trait LetterCase

      case class Default() extends LetterCase

      case class LowerCase() extends LetterCase

      case class UpperCase() extends LetterCase

      case class State(name: String, letterCase: LetterCase)

      override def getInitialState(): State = State(
        name = "whoever you are",
        letterCase = Default()
      )

      var nameElement: ReactHTMLInputElement = _
      var lowerCaseElement: ReactHTMLRadioElement = _
      var upperCaseElement: ReactHTMLRadioElement = _

      override def render() = {
        <.div()(
          <.input(
            ^.id := "name",
            ^.ref := ((element: ReactHTMLInputElement) => {
              nameElement = element
            }),
            ^.value := state.name,
            ^.onChange := onChange
          )(),
          <.input(
            ^.`type` := "radio",
            ^.name := "letter-case",
            ^.value := "Default",
            ^.checked := state.letterCase == Default(),
            ^.onChange := onChange
          )(),
          "Default",
          <.input(
            ^.`type` := "radio",
            ^.name := "letter-case",
            ^.value := "Lower Case",
            ^.ref := ((element: ReactHTMLRadioElement) => {
              lowerCaseElement = element
            }),
            ^.checked := state.letterCase == LowerCase(),
            ^.onChange := onChange
          )(),
          "Lower Case",
          <.input(
            ^.`type` := "radio",
            ^.name := "letter-case",
            ^.value := "Upper Case",
            ^.ref := ((element: ReactHTMLRadioElement) => {
              upperCaseElement = element
            }),
            ^.checked := state.letterCase == UpperCase(),
            ^.onChange := onChange
          )(),
          "Upper Case",
          <.div(^.id := "greet")(s"Hello, $name!")
        )
      }

      val onChange = () => {
        setState(State(
          name = nameElement.value,
          letterCase = if (lowerCaseElement.checked) {
            LowerCase()
          } else if (upperCaseElement.checked) {
            UpperCase()
          } else {
            Default()
          }
        ))
      }

      def name: String = {
        state.letterCase match {
          case LowerCase() => state.name.toLowerCase
          case UpperCase() => state.name.toUpperCase
          case _ => state.name
        }
      }
    }

    ReactDOM.render(InteractiveHelloWorld, element)
  }
}
