package io.github.shogowada.scalajs.reactjs.example.todoappredux

case class State(todos: Seq[TodoItem], visibilityFilter: String)

case class TodoItem(id: Int, completed: Boolean, text: String)
