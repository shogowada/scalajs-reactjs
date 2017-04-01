package io.github.shogowada.scalajs.reactjs.example.todoappredux

import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.specs.StatelessReactClassSpec
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
  case class Props(todos: Seq[TodoItem], onTodoClick: (Int) => Unit)

  def apply(props: Props): ReactElement =
    <.ul()(
      props.todos.map(todo => {
        Todo(Todo.Props(
          todoItem = todo,
          onClick = () => props.onTodoClick(todo.id)
        ))
      })
    )
}

object Link {
  case class Props(active: Boolean, onClick: () => Unit)

  def apply(props: Props, children: ReactElement): ReactElement =
    if (props.active) {
      <.span()(children)
    } else {
      <.a(
        ^.href := "#",
        ^.onClick := ((e: SyntheticEvent) => {
          e.preventDefault()
          props.onClick()
        })
      )(
        children
      )
    }
}

object Footer {
  def apply(): ReactElement =
    <.p()(
      "Show: ",
      <.LinkContainerComponent(LinkContainerComponentProps("SHOW_ALL"))(
        "All"
      ),
      ", ",
      <.LinkContainerComponent(LinkContainerComponentProps("SHOW_ACTIVE"))(
        "Active"
      ),
      ", ",
      <.LinkContainerComponent(LinkContainerComponentProps("SHOW_COMPLETED"))(
        "Completed"
      )
    )
}

class AddTodoComponent extends StatelessReactClassSpec[AddTodoComponent.Props] {

  private var input: ReactHTMLInputElement = _

  override def render() =
    <.div()(
      <.form(
        ^.onSubmit := ((event: InputFormSyntheticEvent) => {
          event.preventDefault()
          if (!input.value.trim.isEmpty) {
            props.onAddTodo(input.value)
            input.value = ""
          }
        })
      )(
        <.input(^.ref := ((node: ReactHTMLInputElement) => input = node))(),
        <.button(^.`type` := "submit")(
          "Add Todo"
        )
      )
    )
}

object AddTodoComponent {
  case class Props(onAddTodo: (String) => Unit)
}

object App {
  def apply(): ReactElement =
    <.div()(
      <.AddTodoContainerComponent(),
      <.TodoListContainerComponent(),
      Footer()
    )
}
