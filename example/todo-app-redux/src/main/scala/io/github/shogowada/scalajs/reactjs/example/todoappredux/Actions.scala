package io.github.shogowada.scalajs.reactjs.example.todoappredux

import io.github.shogowada.scalajs.reactjs.redux.Action

case class AddTodo(
    `type`: String = "ADD_TODO",
    id: Int = AddTodo.nextId,
    text: String
) extends Action

object AddTodo {
  var currentId = 0

  def nextId: Int = {
    currentId += 1
    currentId
  }
}

case class SetVisibilityFilter(
    `type`: String = "SET_VISIBILITY_FILTER",
    filter: String
) extends Action

case class ToggleTodo(
    `type`: String = "TOGGLE_TODO",
    id: Int
) extends Action
