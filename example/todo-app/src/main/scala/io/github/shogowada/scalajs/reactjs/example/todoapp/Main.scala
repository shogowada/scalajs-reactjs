package io.github.shogowada.scalajs.reactjs.example.todoapp

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.specs.{PropslessReactClassSpec, StatelessReactClassSpec}
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.{InputFormSyntheticEvent, SyntheticEvent}
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.JSApp

object Main extends JSApp {
  def main(): Unit = {
    case class Item(id: String, text: String)

    object TodoApp {

      case class State(items: Seq[Item], text: String)

      def apply() = new TodoApp()
    }

    class TodoApp extends PropslessReactClassSpec[TodoApp.State] {

      import TodoApp._

      override def getInitialState() = State(items = Seq(), text = "")

      override def render(): ReactElement = {
        <.div()(
          <.h3()("TODO"),
          TodoList(TodoList.Props(items = state.items)),
          <.form(^.onSubmit := handleSubmit)(
            <.input(^.onChange := handleChange, ^.value := state.text)(),
            <.button()(s"Add #${state.items.size + 1}")
          )
        )
      }

      private val handleChange = (e: InputFormSyntheticEvent) => {
        val newText = e.target.value
        setState(_.copy(text = newText))
      }

      private val handleSubmit = (e: SyntheticEvent) => {
        e.preventDefault()
        val newItem = Item(text = state.text, id = js.Date.now().toString)
        setState((prevState: State) => State(
          items = prevState.items :+ newItem,
          text = ""
        ))
      }
    }

    object TodoList {

      case class Props(items: Seq[Item])

      def apply(props: Props): ReactElement = (new TodoList) (props)()
    }

    class TodoList extends StatelessReactClassSpec[TodoList.Props] {
      override def render(): ReactElement = <.ul()(props.items.map(item => <.li(^.key := item.id)(item.text)))
    }

    val mountNode = dom.document.getElementById("mount-node")
    ReactDOM.render(TodoApp(), mountNode)
  }
}
