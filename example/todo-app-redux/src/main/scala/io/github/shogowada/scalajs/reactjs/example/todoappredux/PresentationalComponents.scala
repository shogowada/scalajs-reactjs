package io.github.shogowada.scalajs.reactjs.example.todoappredux

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.{FormSyntheticEvent, SyntheticEvent}
import io.github.shogowada.scalajs.reactjs.example.todoappredux.ContainerComponents._
import org.scalajs.dom.raw.HTMLInputElement

object Todo {
  case class Props(onClick: () => Unit, todoItem: TodoItem)

  def apply(props: Props) =
    <.li(
      ^.key := props.todoItem.id.toString,
      ^.onClick := props.onClick,
      ^.style := Map(
        "textDecoration" -> (if (props.todoItem.completed) "line-through" else "none")
      )
    )(props.todoItem.text)
}

object TodoList {
  case class WrappedProps(todos: Seq[TodoItem], onTodoClick: (Int) => Unit)

  def apply(props: Props[WrappedProps]): ReactElement =
    <.ul()(
      props.wrapped.todos.map(todo => {
        Todo(Todo.Props(
          todoItem = todo,
          onClick = () => props.wrapped.onTodoClick(todo.id)
        ))
      })
    )
}

object Link {
  case class WrappedProps(active: Boolean, onClick: () => Unit)

  def apply(props: Props[WrappedProps]): ReactElement =
    if (props.wrapped.active) {
      <.span()(props.children)
    } else {
      <.a(
        ^.href := "#",
        ^.onClick := ((e: SyntheticEvent) => {
          e.preventDefault()
          props.wrapped.onClick()
        })
      )(
        props.children
      )
    }
}

object Footer {
  def apply(): ReactElement =
    <.p()(
      "Show: ",
      <(LinkContainerComponent)(
        // Make sure to wrap own props with "wrapped" property
        ^.wrapped := LinkContainerComponentOwnProps("SHOW_ALL")
      )(
        "All"
      ),
      ", ",
      <(LinkContainerComponent)(
        ^.wrapped := LinkContainerComponentOwnProps("SHOW_ACTIVE")
      )(
        "Active"
      ),
      ", ",
      <(LinkContainerComponent)(
        ^.wrapped := LinkContainerComponentOwnProps("SHOW_COMPLETED")
      )(
        "Completed"
      )
    )
}

object AddTodoComponent {
  case class WrappedProps(onAddTodo: (String) => Unit)

  def apply() = reactClass

  private lazy val reactClass = React.createClass[WrappedProps, Unit](
    (self) => {
      var input: HTMLInputElement = null
      <.div()(
        <.form(
          ^.onSubmit := ((event: FormSyntheticEvent[_]) => {
            event.preventDefault()
            if (!input.value.trim.isEmpty) {
              self.props.wrapped.onAddTodo(input.value)
              input.value = ""
            }
          })
        )(
          <.input(^.ref := ((node: HTMLInputElement) => input = node))(),
          <.button(^.`type`.submit)(
            "Add Todo"
          )
        )
      )
    }
  )
}

object App {
  def apply(): ReactElement =
    <.div()(
      <(AddTodoContainerComponent).empty,
      <(TodoListContainerComponent).empty,
      Footer()
    )
}
