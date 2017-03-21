package io.github.shogowada.scalajs.reactjs.example.todoappredux

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.specs.StatelessReactClassSpec
import io.github.shogowada.scalajs.reactjs.elements.{ReactElement, ReactHTMLInputElement}
import io.github.shogowada.scalajs.reactjs.events.{InputFormSyntheticEvent, SyntheticEvent}
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux._
import io.github.shogowada.scalajs.reactjs.redux.{Action, Redux}
import org.scalajs.dom

import scala.scalajs.js.JSApp

case class State(todos: Seq[TodoItem], visibilityFilter: String)

case class TodoItem(id: Int, completed: Boolean, text: String)

class Todo extends StatelessReactClassSpec {
  override type Props = Todo.Props

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

class TodoList extends StatelessReactClassSpec {

  override type Props = TodoList.Props

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

class Link extends StatelessReactClassSpec {
  override type Props = Link.Props

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

class Footer extends StatelessReactClassSpec {
  override type Props = Footer.Props

  override def render() = {
    <.p()(
      "Show: ",
      FilterLink()(FilterLink.Props("SHOW_ALL"))(
        "All"
      ),
      ", ",
      FilterLink()(FilterLink.Props("SHOW_ACTIVE"))(
        "Active"
      ),
      ", ",
      FilterLink()(FilterLink.Props("SHOW_COMPLETED"))(
        "Completed"
      )
    )
  }
}

object Footer {

  case class Props()

  def apply() = new Footer()
}

class AddTodoComponent extends StatelessReactClassSpec {
  override type Props = AddTodoComponent.Props

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

class App extends StatelessReactClassSpec {

  override type Props = App.Props

  override def render() = {
    <.div()(
      (AddTodoContainerComponent()) ()(),
      (TodoListContainerComponent()) ()(),
      Footer()
    )
  }
}

object App {

  case class Props()

  def apply(): ReactElement = (new App) (Props())()

}

object Main extends JSApp {
  override def main(): Unit = {
    val mountNode = dom.document.getElementById("mount-node")

    val store = Redux.createStore((state: State, action: Action) => state)

    ReactDOM.render(
      <.Provider(store = store)(
        App()
      ),
      mountNode
    )
  }
}
