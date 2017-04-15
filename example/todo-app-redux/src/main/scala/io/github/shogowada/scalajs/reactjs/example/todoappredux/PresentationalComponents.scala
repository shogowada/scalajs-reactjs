package io.github.shogowada.scalajs.reactjs.example.todoappredux

import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.specs.{Props, StatelessReactClassSpec}
import io.github.shogowada.scalajs.reactjs.elements.{ReactElement, ReactHTMLInputElement}
import io.github.shogowada.scalajs.reactjs.events.{InputFormSyntheticEvent, SyntheticEvent}
import io.github.shogowada.scalajs.reactjs.example.todoappredux.ContainerComponents._

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
      <.LinkContainerComponent(
        // Make sure to wrap own props with "wrapped" property
        ^.wrapped := LinkContainerComponentOwnProps("SHOW_ALL")
      )(
        "All"
      ),
      ", ",
      <.LinkContainerComponent(
        ^.wrapped := LinkContainerComponentOwnProps("SHOW_ACTIVE")
      )(
        "Active"
      ),
      ", ",
      <.LinkContainerComponent(
        ^.wrapped := LinkContainerComponentOwnProps("SHOW_COMPLETED")
      )(
        "Completed"
      )
    )
}

class AddTodoComponent extends StatelessReactClassSpec[AddTodoComponent.WrappedProps] {

  private var input: ReactHTMLInputElement = _

  override def render() =
    <.div()(
      <.form(
        ^.onSubmit := ((event: InputFormSyntheticEvent) => {
          event.preventDefault()
          if (!input.value.trim.isEmpty) {
            props.wrapped.onAddTodo(input.value)
            input.value = ""
          }
        })
      )(
        <.input(^.ref := ((node: ReactHTMLInputElement) => input = node))(),
        <.button(^.`type`.submit)(
          "Add Todo"
        )
      )
    )
}

object AddTodoComponent {
  case class WrappedProps(onAddTodo: (String) => Unit)
}

object App {
  def apply(): ReactElement =
    <.div()(
      <.AddTodoContainerComponent.empty,
      <.TodoListContainerComponent.empty,
      Footer()
    )
}
