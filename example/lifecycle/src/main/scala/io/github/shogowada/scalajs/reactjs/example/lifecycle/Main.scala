package io.github.shogowada.scalajs.reactjs.example.lifecycle

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.specs.{Props, PropslessReactClassSpec}
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.scalajs.dom

import scala.scalajs.js.JSApp

object App {
  case class State(
      componentDidMountCalled: Boolean,
      componentDidUpdateCalled: Boolean
  )
}

class App extends PropslessReactClassSpec[App.State] {

  import App._

  override def componentWillMount(): Unit = {
    println("componentWillMount()")
  }

  override def componentDidMount(): Unit = {
    println("componentDidMount()")
    setState(_.copy(componentDidMountCalled = true))
  }

  override def shouldComponentUpdate(nextProps: Props[Unit], nextState: State): Boolean = {
    println(s"shouldComponentUpdate($nextProps, $nextState)")
    nextState != state
  }

  override def componentWillReceiveProps(nextProps: Props[Unit]): Unit = {
    println(s"componentWillReceiveProps($nextProps)")
  }

  override def componentWillUpdate(nextProps: Props[Unit], nextState: State): Unit = {
    println(s"componentWillUpdate($nextProps, $nextState)")
  }

  override def componentDidUpdate(prevProps: Props[Unit], prevState: State): Unit = {
    println(s"componentDidUpdate($prevProps, $prevState)")
    setState(_.copy(componentDidUpdateCalled = true))
  }

  override def componentWillUnmount(): Unit = {
    println("componentWillUnmount()")
  }

  override def getInitialState() = State(
    componentDidMountCalled = false,
    componentDidUpdateCalled = false
  )

  override def render(): ReactElement = {
    <.div()(
      <.label(^.`for` := "component-did-mount")("Component Did Mount: "),
      <.input(^.id := "component-did-mount", ^.`type`.checkbox, ^.checked := state.componentDidMountCalled)(),
      <.label(^.`for` := "component-did-update")("Component Did Update: "),
      <.input(^.id := "component-did-update", ^.`type`.checkbox, ^.checked := state.componentDidUpdateCalled)()
    ).asReactElement
  }
}

object Main extends JSApp {
  override def main(): Unit = {
    val mountNode = dom.document.getElementById("mount-node")
    ReactDOM.render(<(new App()).empty, mountNode)
  }
}
