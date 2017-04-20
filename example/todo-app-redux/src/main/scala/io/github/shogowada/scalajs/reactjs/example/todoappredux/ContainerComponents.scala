package io.github.shogowada.scalajs.reactjs.example.todoappredux

import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch

/*
* You can create container components by using ReactRedux.connectAdvanced method.
*
* You can pass either one of the following to create a component:
*
* - Render function of type (props: Props[WrappedProps]) => ReactElement
* - Presentational component of type ReactClass
*
* It shows example for both.
* */

object ContainerComponents {

  case class LinkContainerComponentOwnProps(filter: String)

  def LinkContainerComponent: ReactClass = ReactRedux.connectAdvanced(
    (dispatch: Dispatch) => {
      var ownProps: LinkContainerComponentOwnProps = null
      val onClick: () => Unit = () => dispatch(SetVisibilityFilter(filter = ownProps.filter))

      (state: State, nextOwnProps: LinkContainerComponentOwnProps) => {
        ownProps = nextOwnProps
        Link.WrappedProps(
          active = ownProps.filter == state.visibilityFilter,
          onClick = onClick
        )
      }
    }
  )(Link(_)) // (Props[WrappedProps]) => ReactElement

  def TodoListContainerComponent: ReactClass = ReactRedux.connectAdvanced(
    (dispatch: Dispatch) => {
      val onTodoClick: (Int) => Unit = (id: Int) => dispatch(ToggleTodo(id = id))
      (state: State, ownProps: Unit) => {
        TodoList.WrappedProps(
          todos = state.visibilityFilter match {
            case VisibilityFilters.ShowAll => state.todos
            case VisibilityFilters.ShowActive => state.todos.filter(todo => !todo.completed)
            case VisibilityFilters.ShowCompleted => state.todos.filter(todo => todo.completed)
          },
          onTodoClick = onTodoClick
        )
      }
    }
  )(TodoList(_)) // (Props[WrappedProps]) => ReactElement

  def AddTodoContainerComponent: ReactClass = ReactRedux.connectAdvanced(
    (dispatch: Dispatch) => {
      val onAddTodo: (String) => Unit = (text: String) => dispatch(AddTodo(text = text))
      (state: State, ownProps: Unit) =>
        AddTodoComponent.WrappedProps(
          onAddTodo = onAddTodo
        )
    }
  )(AddTodoComponent()) // ReactClass
}
