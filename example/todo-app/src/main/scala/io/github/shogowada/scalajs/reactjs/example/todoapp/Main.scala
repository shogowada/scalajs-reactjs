package io.github.shogowada.scalajs.reactjs.example.todoapp

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.specs.{ReactClassSpec, StatelessReactClassSpec}
import io.github.shogowada.scalajs.reactjs.events.{InputElementSyntheticEvent, SyntheticEvent}
import org.scalajs.dom.raw.HTMLElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

@JSExport
class Main {
  @JSExport
  def main(mountNode: HTMLElement): Unit = {
    case class Item(id: String, text: String)

    class TodoApp extends ReactClassSpec {

      case class Props()

      case class State(items: Seq[Item], text: String)

      override def getInitialState() = State(items = Seq(), text = "")

      override def render() = {
        <.div()(
          <.h3()("TODO"),
          <.reactElement(new TodoList(), TodoList.Props(items = state.items)),
          <.form(^.onSubmit := handleSubmit)(
            <.input(^.onChange := handleChange, ^.value := state.text)(),
            <.button()(s"Add #${state.items.size + 1}")
          )
        )
      }

      val handleChange = (event: InputElementSyntheticEvent) => {
        val newText = event.target.value
        setState(_.copy(text = newText))
      }

      val handleSubmit = (event: SyntheticEvent) => {
        event.preventDefault()
        val newItem = Item(text = state.text, id = js.Date.now().toString)
        setState((previousState: State) => State(
          items = previousState.items :+ newItem,
          text = ""
        ))
      }
    }

    class TodoList extends StatelessReactClassSpec {

      override type Props = TodoList.Props

      override def render() = <.ul()(props.items.map(item => <.li(^.key := item.id)(item.text)))
    }

    object TodoList {

      case class Props(items: Seq[Item])

    }

    ReactDOM.render(<.reactElement(new TodoApp()), mountNode)
  }
}
