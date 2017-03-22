package io.github.shogowada.scalajs.reactjs.example.todoappredux

import io.github.shogowada.scalajs.reactjs.redux.Action

object Reducer {
  def reduce(maybeState: Option[State], action: Action): State = {
    maybeState.fold(
      State(
        todos = Seq.empty,
        visibilityFilter = VisibilityFilters.ShowAll
      )
    )(state => reduce(state, action))
  }

  private def reduce(state: State, action: Action): State = {
    action match {
      case action: AddTodo => {
        val newTodo = TodoItem(
          id = action.id,
          text = action.text,
          completed = false
        )
        state.copy(todos = state.todos :+ newTodo)
      }
      case action: SetVisibilityFilter => {
        state.copy(visibilityFilter = action.filter)
      }
      case action: ToggleTodo => {
        val newTodos = state.todos
            .map(todo => if (todo.id == action.id) {
              todo.copy(completed = !todo.completed)
            } else {
              todo
            })
        state.copy(todos = newTodos)
      }
      case _ => state
    }
  }
}
