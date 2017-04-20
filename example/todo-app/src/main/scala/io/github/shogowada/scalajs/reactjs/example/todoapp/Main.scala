package io.github.shogowada.scalajs.reactjs.example.todoapp

import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.{InputFormSyntheticEvent, SyntheticEvent}
import io.github.shogowada.scalajs.reactjs.{React, ReactDOM}
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

  type Self = React.Self[Unit, State]

  def apply() = reactClass

  private lazy val reactClass = React.createClass[Unit, State](
    getInitialState = (self) => State(items = Seq.empty, text = ""),
    render = (self) =>
      <.div()(
        <.h3()("TODO"),
        TodoList(self.state.items),
        <.form(^.onSubmit := handleSubmit(self))(
          <.input(^.onChange := handleChange(self), ^.value := self.state.text)(),
          <.button()(s"Add #${self.state.items.size + 1}")
        )
      )
  )

  private def handleChange(self: Self) =
    (e: InputFormSyntheticEvent) => {
      // Cache the value because React reuses the event object.
      val newText = e.target.value
      // It is a syntactic sugar for setState((prevState: State) => prevState.copy(text = newText))
      self.setState(_.copy(text = newText))
    }

  private def handleSubmit(self: Self) =
    (e: SyntheticEvent) => {
      e.preventDefault()
      val newItem = Item(text = self.state.text, id = js.Date.now().toString)
      self.setState((prevState: State) => State(
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
