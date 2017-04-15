package io.github.shogowada.scalajs.reactjs.example.todoapp

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.specs.PropslessReactClassSpec
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.{InputFormSyntheticEvent, SyntheticEvent}
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.JSApp

object Main extends JSApp {
  def main(): Unit = {
    val mountNode = dom.document.getElementById("mount-node")
    ReactDOM.render(
      <(TodoApp()).empty,
      mountNode
    )
  }
}

case class Item(id: String, text: String)

object TodoApp {
  case class State(items: Seq[Item], text: String)

  def apply() = new TodoApp()
}

class TodoApp extends PropslessReactClassSpec[TodoApp.State] {

  import TodoApp._

  override def getInitialState() = State(items = Seq.empty, text = "")

  override def render(): ReactElement =
    <.div()(
      <.h3()("TODO"),
      TodoList(state.items),
      <.form(^.onSubmit := handleSubmit)(
        <.input(^.onChange := handleChange, ^.value := state.text)(),
        <.button()(s"Add #${state.items.size + 1}")
      )
    )

  private val handleChange = (e: InputFormSyntheticEvent) => {
    // Cache the value because React reuses the event object.
    val newText = e.target.value
    // It is a syntactic sugar for setState((prevState: State) => prevState.copy(text = newText))
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
  // Use a pure function to render
  def apply(items: Seq[Item]): ReactElement =
    <.ul()(items.map(item => <.li(^.key := item.id)(item.text)))
}
