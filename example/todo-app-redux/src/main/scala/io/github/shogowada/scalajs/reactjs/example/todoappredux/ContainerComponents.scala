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
    def LinkContainerComponent = ReactRedux.connectAdvanced(
      (dispatch: Dispatch) => {
        var ownProps: LinkContainerComponentProps = null
        val onClick: () => Unit = () => dispatch(SetVisibilityFilter(filter = ownProps.filter))
        (state: State, nextOwnProps: LinkContainerComponentProps) => {
          ownProps = nextOwnProps
          Link.Props(
            active = ownProps.filter == state.visibilityFilter,
            onClick = onClick
          )
        }
      }
    )(Link(_, _)) // (Props, ReactElement) => ReactElement

    def TodoListContainerComponent = ReactRedux.connectAdvanced(
      (dispatch: Dispatch) => {
        val onTodoClick: (Int) => Unit = (id: Int) => dispatch(ToggleTodo(id = id))
        (state: State, ownProps: Unit) => {
          TodoList.Props(
            todos = state.visibilityFilter match {
              case VisibilityFilters.ShowAll => state.todos
              case VisibilityFilters.ShowActive => state.todos.filter(todo => !todo.completed)
              case VisibilityFilters.ShowCompleted => state.todos.filter(todo => todo.completed)
            },
            onTodoClick = onTodoClick
          )
        }
      }
    )(TodoList(_)) // (Props) => ReactElement

    def AddTodoContainerComponent = ReactRedux.connectAdvanced(
      (dispatch: Dispatch) => {
        val onAddTodo: (String) => Unit = (text: String) => dispatch(AddTodo(text = text))
        (state: State, ownProps: Unit) =>
          AddTodoComponent.Props(
            onAddTodo = onAddTodo
          )
      }
    )(new AddTodoComponent()) // ReactClassSpec
  }

}
