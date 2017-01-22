package io.github.shogowada.scalajs.reactjs.example.todoapp

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.specs.{ReactClassSpec, StatelessReactClassSpec}
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.{InputFormSyntheticEvent, SyntheticEvent}
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.JSApp

object Main extends JSApp {
  def main(): Unit = {
    case class Item(id: String, text: String)

    class TodoList extends StatelessReactClassSpec {

      override type Props = TodoList.Props

      override def render(): ReactElement = <.ul()(props.items.map(item => <.li(^.key := item.id)(item.text)))
    }

    object TodoList {

      case class Props(items: Seq[Item])

    }

    class TodoApp extends ReactClassSpec {

      case class Props()

      case class State(items: Seq[Item], text: String)

      override def getInitialState() = State(items = Seq(), text = "")

      override def render(): ReactElement = {
        <.div()(
          <.h3()("TODO"),
          new TodoList()(TodoList.Props(items = state.items)),
          <.form(^.onSubmit := handleSubmit)(
            <.input(^.onChange := handleChange, ^.value := state.text)(),
            <.button()(s"Add #${state.items.size + 1}")
          )
        )
      }

      private val handleChange = (event: InputFormSyntheticEvent) => {
        val newText = event.target.value
        setState(_.copy(text = newText))
      }

      private val handleSubmit = (event: SyntheticEvent) => {
        event.preventDefault()
        val newItem = Item(text = state.text, id = js.Date.now().toString)
        setState((previousState: State) => State(
          items = previousState.items :+ newItem,
          text = ""
        ))
      }
    }

    val mountNode = dom.document.getElementById("mount-node")
    ReactDOM.render(new TodoApp(), mountNode)
  }
}
