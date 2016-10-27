package io.github.shogowada.scalajs.reactjs.example.interactive.helloworld

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.specs.ReactClassSpec
import io.github.shogowada.scalajs.reactjs.elements.ReactHTMLInputElement
import org.scalajs.dom.raw.HTMLElement

import scala.scalajs.js.annotation.JSExport

@JSExport
object Main {

  @JSExport
  def main(element: HTMLElement): Unit = {
    object InteractiveHelloWorld extends ReactClassSpec {

      case class State(name: String)

      override def getInitialState(): State = State(name = "whoever you are")

      var nameElement: ReactHTMLInputElement = _

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
          <.div(^.id := "greet")(s"Hello, ${state.name}!")
        )
      }

      val onChange = () => {
        setState(State(name = nameElement.value))
      }
    }

    ReactDOM.render(InteractiveHelloWorld, element)
  }
}
