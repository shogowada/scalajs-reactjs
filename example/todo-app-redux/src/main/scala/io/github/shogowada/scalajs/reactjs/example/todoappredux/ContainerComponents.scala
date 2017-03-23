package io.github.shogowada.scalajs.reactjs.example.todoappredux

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch

/*
* You can create container components by using ReactRedux.connect method.
* The method has three signatures:
*
* - ReactRedux.connect(dispatch, state, ownProps)
* - ReactRedux.connect(dispatch, state)
* - ReactRedux.connect(dispatch)
*
* It shows example for all of them.
*
* Container components are higher-order components.
* This means you need to pass another component to it to create a real component.
*
* You can pass either one of the following to create a real component:
*
* - Render function of type (props: Props, children: ReactElement) => ReactElement
* - Render function of type (props: Props) => ReactElement
* - Presentational component of type ReactClassSpec
*
* It shows example for all of them.
* */

object ContainerComponents {

  case class LinkContainerComponentProps(filter: String)

  implicit class RichVirtualDOMElements(virtualDOMElements: VirtualDOMElements) {
    def LinkContainerComponent = ReactRedux.connect(
      (dispatch: Dispatch, state: State, ownProps: LinkContainerComponentProps) => {
        Link.Props(
          active = ownProps.filter == state.visibilityFilter,
          onClick = () => {
            dispatch(SetVisibilityFilter(filter = ownProps.filter))
          }
        )
      }
    )(Link(_, _)) // (Props, ReactElement) => ReactElement

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
    )(TodoList(_)) // (Props) => ReactElement

    def AddTodoContainerComponent = ReactRedux.connect(
      (dispatch: Dispatch) => {
        AddTodoComponent.Props(
          onAddTodo = (text: String) => {
            dispatch(AddTodo(text = text))
          }
        )
      }
    )(new AddTodoComponent()) // ReactClassSpec
  }

}
