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
  def main(element: HTMLElement): Unit = {
    case class Item(id: String, text: String)

    class TodoApp extends ReactClassSpec {

      case class Props()

      case class State(items: Seq[Item], text: String)

      val todoList = new TodoList()

      override def getInitialState() = State(items = Seq(), text = "")

      override def render() = {
        <.div()(
          <.h3()("TODO"),
          <.reactElement(todoList, todoList.Props(items = state.items)),
          <.form(^.onSubmit := handleSubmit)(
            <.input(^.onChange := handleChange, ^.value := state.text)(),
            <.button()(s"Add #${state.items.size + 1}")
          )
        )
      }

      val handleChange = (event: InputElementSyntheticEvent) => {
        setState(state.copy(text = event.target.value))
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

      case class Props(items: Seq[Item])

      override def render() = <.ul()(props.items.map(item => <.li(^.key := item.id)(item.text)))
    }

    ReactDOM.render(new TodoApp(), element)
  }
}
