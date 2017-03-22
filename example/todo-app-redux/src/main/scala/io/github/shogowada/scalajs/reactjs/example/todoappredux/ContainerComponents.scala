package io.github.shogowada.scalajs.reactjs.example.todoappredux

import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch

object ContainerComponents {

  case class LinkContainerComponentProps(filter: String)

  def LinkContainerComponent = ReactRedux.connect(
    (dispatch: Dispatch, state: State, ownProps: LinkContainerComponentProps) => {
      Link.Props(
        active = ownProps.filter == state.visibilityFilter,
        onClick = () => {
          dispatch(SetVisibilityFilter(filter = ownProps.filter))
        }
      )
    }
  )(new Link())

  def TodoListContainerComponent = ReactRedux.connect(
    (dispatch: Dispatch, state: State) => {
      TodoList.Props(
        todos = state.visibilityFilter match {
          case VisibilityFilters.ShowAll => state.todos
          case VisibilityFilters.ShowActive => state.todos.filter(todo => !todo.completed)
          case VisibilityFilters.ShowCompleted => state.todos.filter(todo => todo.completed)
        },
        onTodoClick = (id: Int) => {
          dispatch(ToggleTodo(id = id))
        }
      )
    }
  )(new TodoList())

  def AddTodoContainerComponent = ReactRedux.connect(
    (dispatch: Dispatch, state: State) => {
      AddTodoComponent.Props(
        onAddTodo = (text: String) => {
          dispatch(AddTodo(text = text))
        }
      )
    }
  )(new AddTodoComponent())
}
