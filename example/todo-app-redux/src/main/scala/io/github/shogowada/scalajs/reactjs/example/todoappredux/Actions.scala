package io.github.shogowada.scalajs.reactjs.example.todoappredux

import io.github.shogowada.scalajs.reactjs.redux.Action

object VisibilityFilters {
  val ShowAll = "SHOW_ALL"
  val ShowActive = "SHOW_ACTIVE"
  val ShowCompleted = "SHOW_COMPLETED"
}

case class AddTodo(
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

case class SetVisibilityFilter(filter: String) extends Action

case class ToggleTodo(id: Int) extends Action
