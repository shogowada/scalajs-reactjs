package io.github.shogowada.scalajs.reactjs.example.todoappredux

import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.redux.{Action, Redux}

case class SetVisibilityFilter(filter: String) extends Action {
  override val `type` = "SET_VISIBILITY_FILTER"
}

case class ToggleTodo(id: String) extends Action {
  override val `type` = "TOGGLE_TODO"
}

object ContainerComponents {
  def FilterLink = Redux.connectAdvanced((dispatch: Dispatch) => {
    (state: State, ownProps: FilterLinkProps) => {
      Link.Props(
        active = ownProps.filter == state.visibilityFilter,
        onClick = () => {
          dispatch(SetVisibilityFilter(ownProps.filter))
        }
      )
    }
  })(new Link())

  case class FilterLinkProps(filter: String)

  def VisibleTodoList = Redux.connectAdvanced((dispatch: Dispatch) => {
    (state: State, ownProps: Unit) => {
      TodoList.Props(
        todos = state.visibilityFilter match {
          case "SHOW_ALL" => state.todos
          case "SHOW_ACTIVE" => state.todos.filter(todo => !todo.completed)
          case "SHOW_COMPLETED" => state.todos.filter(todo => todo.completed)
        },
        onTodoClick = (id: String) => {
          dispatch(ToggleTodo(id))
        }
      )
    }
  })(new TodoList())

}
