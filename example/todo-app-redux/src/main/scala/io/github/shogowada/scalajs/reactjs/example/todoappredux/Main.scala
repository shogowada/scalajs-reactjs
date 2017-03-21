package io.github.shogowada.scalajs.reactjs.example.todoappredux

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux._
import io.github.shogowada.scalajs.reactjs.redux.Redux
import org.scalajs.dom

import scala.scalajs.js.JSApp

case class State(todos: Seq[TodoItem], visibilityFilter: String)

case class TodoItem(id: Int, completed: Boolean, text: String)

object Main extends JSApp {
  override def main(): Unit = {
    val mountNode = dom.document.getElementById("mount-node")

    val store = Redux.createStore(Reducer.reduce)

    ReactDOM.render(
      <.Provider(store = store)(
        App()
      ),
      mountNode
    )
  }
}
