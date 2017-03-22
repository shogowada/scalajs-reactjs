package io.github.shogowada.scalajs.reactjs.example.todoappredux

import io.github.shogowada.scalajs.reactjs.redux.Action

object Reducer {
  def reduce(maybeState: Option[State], action: Action): State = {
    maybeState.fold(initialState)(state => reduce(state, action))
  }

  def initialState = State(
    todos = Seq.empty,
    visibilityFilter = VisibilityFilters.ShowAll
  )

  private def reduce(state: State, action: Action): State = {
    state.copy(
      todos = reduceTodos(state.todos, action),
      visibilityFilter = reduceVisibilityFilter(state.visibilityFilter, action)
    )
  }

  private def reduceTodos(todos: Seq[TodoItem], action: Action): Seq[TodoItem] = {
    action match {
      case action: AddTodo => {
        val newTodo = TodoItem(
          id = action.id,
          text = action.text,
          completed = false
        )
        todos :+ newTodo
      }
      case action: ToggleTodo => {
        todos
            .map(todo => if (todo.id == action.id) {
              todo.copy(completed = !todo.completed)
            } else {
              todo
            })
      }
      case _ => todos
    }
  }

  private def reduceVisibilityFilter(visibilityFilter: String, action: Action): String = {
    action match {
      case action: SetVisibilityFilter => action.filter
      case _ => visibilityFilter
    }
  }
}
