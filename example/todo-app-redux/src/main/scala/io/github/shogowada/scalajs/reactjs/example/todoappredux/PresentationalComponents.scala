package io.github.shogowada.scalajs.reactjs.example.todoappredux

import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.specs.{StatelessReactClassSpec, StaticReactClassSpec}
import io.github.shogowada.scalajs.reactjs.elements.{ReactElement, ReactHTMLInputElement}
import io.github.shogowada.scalajs.reactjs.events.{InputFormSyntheticEvent, SyntheticEvent}
import io.github.shogowada.scalajs.reactjs.example.todoappredux.ContainerComponents._

class Todo extends StatelessReactClassSpec[Todo.Props] {

  override def render() = {
    <.li(
      ^.key := props.todoItem.id.toString,
      ^.onClick := props.onClick,
      ^.style := Map(
        "textDecoration" -> (if (props.todoItem.completed) "line-through" else "none")
      )
    )(props.todoItem.text)
  }
}

object Todo {

  case class Props(onClick: () => Unit, todoItem: TodoItem)

  def apply(props: Props) = new Todo()(props)()
}

class TodoList extends StatelessReactClassSpec[TodoList.Props] {

  override def render() = {
    <.ul()(
      props.todos.map(todo => {
        Todo(Todo.Props(
          todoItem = todo,
          onClick = () => props.onTodoClick(todo.id)
        ))
      })
    )
  }
}

object TodoList {

  case class Props(todos: Seq[TodoItem], onTodoClick: (Int) => Unit)

}

class Link extends StatelessReactClassSpec[Link.Props] {

  override def render() = {
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
}

object Link {

  case class Props(active: Boolean, onClick: () => Unit)

}

class Footer extends StaticReactClassSpec {
  override def render() = {
    <.p()(
      "Show: ",
      LinkContainerComponent(LinkContainerComponentProps("SHOW_ALL"))(
        "All"
      ),
      ", ",
      LinkContainerComponent(LinkContainerComponentProps("SHOW_ACTIVE"))(
        "Active"
      ),
      ", ",
      LinkContainerComponent(LinkContainerComponentProps("SHOW_COMPLETED"))(
        "Completed"
      )
    )
  }
}

object Footer {
  def apply() = new Footer()
}

class AddTodoComponent extends StatelessReactClassSpec[AddTodoComponent.Props] {

  private var input: ReactHTMLInputElement = _

  override def render() = {
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
    ).asReactElement
  }
}

object AddTodoComponent {

  case class Props(onAddTodo: (String) => Unit)

}

class App extends StatelessReactClassSpec[App.Props] {

  override def render() = {
    <.div()(
      AddTodoContainerComponent(),
      TodoListContainerComponent(),
      Footer()
    )
  }
}

object App {

  case class Props()

  def apply(): ReactElement = (new App) (Props())()

}
