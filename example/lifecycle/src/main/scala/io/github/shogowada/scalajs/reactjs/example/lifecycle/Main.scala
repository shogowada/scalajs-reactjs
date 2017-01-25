package io.github.shogowada.scalajs.reactjs.example.lifecycle

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.specs.ReactClassSpec
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.scalajs.dom

import scala.scalajs.js.JSApp

object App {

  case class Props()

  case class State(
      componentDidMountCalled: Boolean,
      componentDidUpdateCalled: Boolean
  )

}

class App extends ReactClassSpec {

  import App._

  override type Props = App.Props
  override type State = App.State

  override def componentWillMount(): Unit = {
    println("componentWillMount()")
  }

  override def componentDidMount(): Unit = {
    println("componentDidMount()")
    setState(_.copy(componentDidMountCalled = true))
  }

  override def shouldComponentUpdate(nextProps: Props, nextState: State): Boolean = {
    println(s"shouldComponentUpdate($nextProps, $nextState)")
    nextState != state
  }

  override def componentWillReceiveProps(nextProps: Props): Unit = {
    println(s"componentWillReceiveProps($nextProps)")
  }

  override def componentWillUpdate(nextProps: Props, nextState: State): Unit = {
    println(s"componentWillUpdate($nextProps, $nextState)")
  }

  override def componentDidUpdate(prevProps: Props, prevState: State): Unit = {
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
      <.input(^.id := "component-did-mount", ^.`type` := "checkbox", ^.checked := state.componentDidMountCalled)(),
      <.label(^.`for` := "component-did-update")("Component Did Update: "),
      <.input(^.id := "component-did-update", ^.`type` := "checkbox", ^.checked := state.componentDidUpdateCalled)()
    ).asReactElement
  }
}

object Main extends JSApp {
  override def main(): Unit = {
    val mountNode = dom.document.getElementById("mount-node")
    ReactDOM.render(new App()(App.Props()), mountNode)
  }
}
