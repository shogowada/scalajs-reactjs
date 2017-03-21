package io.github.shogowada.scalajs.reactjs.example.todoappredux

import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch

object FilterLink {

  case class Props(filter: String)

  def apply() = ReactRedux.connect(
    (dispatch: Dispatch, state: State, ownProps: Props) => {
      Link.Props(
        active = ownProps.filter == state.visibilityFilter,
        onClick = () => {
          dispatch(SetVisibilityFilter(filter = ownProps.filter))
        }
      )
    }
  )(new Link())
}

object TodoListContainerComponent {
  def apply() = ReactRedux.connect(
    (dispatch: Dispatch, state: State, ownProps: Unit) => {
      TodoList.Props(
        todos = state.visibilityFilter match {
          case "SHOW_ALL" => state.todos
          case "SHOW_ACTIVE" => state.todos.filter(todo => !todo.completed)
          case "SHOW_COMPLETED" => state.todos.filter(todo => todo.completed)
        },
        onTodoClick = (id: Int) => {
          dispatch(ToggleTodo(id = id))
        }
      )
    }
  )(new TodoList())
}

object AddTodoContainerComponent {
  def apply() = ReactRedux.connect(
    (dispatch: Dispatch, state: State, ownProps: Unit) => {
      AddTodoComponent.Props(
        onAddTodo = (text: String) => {
          dispatch(AddTodo(text = text))
        }
      )
    }
  )(new AddTodoComponent())
}
