package io.github.shogowada.scalajs.reactjs.example.todoappredux

import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.specs.StatelessReactClassSpec
import io.github.shogowada.scalajs.reactjs.events.SyntheticEvent
import io.github.shogowada.scalajs.reactjs.example.todoappredux.ContainerComponents.FilterLinkProps
import org.scalajs.dom

import scala.scalajs.js.JSApp

case class State(todos: Seq[TodoItem], visibilityFilter: String)

case class TodoItem(id: String, completed: Boolean, text: String)

class Todo extends StatelessReactClassSpec {
  override type Props = Todo.Props

  override def render() = {
    <.li(
      ^.key := props.todoItem.id,
      ^.onClick := props.onClick,
      ^.style := Map(
        "textDecoration" -> (if (props.todoItem.completed) "line-through" else "none")
      )
    )(props.todoItem.text)
  }
}

object Todo {

  case class Props(onClick: () => Unit, todoItem: TodoItem)

}

class TodoList extends StatelessReactClassSpec {

  override type Props = TodoList.Props

  override def render() = {
    <.ul()(
      props.todos.map(todo => {
        new Todo()(Todo.Props(
          todoItem = todo,
          onClick = () => props.onTodoClick(todo.id)
        ))
      })
    )
  }
}

object TodoList {

  case class Props(todos: Seq[TodoItem], onTodoClick: (String) => Unit)

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
      ContainerComponents.FilterLink(
        ownProps = FilterLinkProps("SHOW_ALL"),
        children = "All"
      ),
      ", ",
      ContainerComponents.FilterLink(
        ownProps = FilterLinkProps("SHOW_ACTIVE"),
        children = "Active"
      ),
      ", ",
      ContainerComponents.FilterLink(
        ownProps = FilterLinkProps("SHOW_COMPLETED"),
        children = "Completed"
      )
    )
  }
}

object Footer {

  case class Props()

}

class App extends StatelessReactClassSpec {
  def render() = {
    <.div()(
      ContainerComponents.VisibleTodoList(),
      new Footer()
    )
  }
}

object Main extends JSApp {
  override def main(): Unit = {
    val mountNode = dom.document.getElementById("mount-node")
  }
}
