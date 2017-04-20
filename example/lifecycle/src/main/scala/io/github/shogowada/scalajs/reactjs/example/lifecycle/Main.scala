package io.github.shogowada.scalajs.reactjs.example.lifecycle

import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.{React, ReactDOM}
import org.scalajs.dom

import scala.scalajs.js.JSApp

object App {
  case class State(
      componentDidMountCalled: Boolean,
      componentDidUpdateCalled: Boolean
  )

  type Context = React.Context[Unit, State]

  def apply() = reactClass

  private lazy val reactClass = React.createClass[Unit, State](
    componentWillMount = (context) => println("componentWillMount()"),
    componentDidMount = (context) => {
      println("componentDidMount()")
      context.setState(_.copy(componentDidMountCalled = true))
    },
    shouldComponentUpdate = (context, nextProps, nextState) => {
      println(s"shouldComponentUpdate($nextProps, $nextState)")
      nextState != context.state
    },
    componentWillReceiveProps = (context, nextProps) => {
      println(s"componentWillReceiveProps($nextProps)")
    },
    componentWillUpdate = (context, nextProps, nextState) => {
      println(s"componentWillUpdate($nextProps, $nextState)")
    },
    componentDidUpdate = (context, prevProps, prevState) => {
      println(s"componentDidUpdate($prevProps, $prevState)")
      context.setState(_.copy(componentDidUpdateCalled = true))
    },
    componentWillUnmount = (context) => {
      println("componentWillUnmount()")
    },
    getInitialState = (context) => State(
      componentDidMountCalled = false,
      componentDidUpdateCalled = false
    ),
    render = (context) =>
      <.div()(
        <.label(^.`for` := "component-did-mount")("Component Did Mount: "),
        <.input(^.id := "component-did-mount", ^.`type`.checkbox, ^.checked := context.state.componentDidMountCalled)(),
        <.label(^.`for` := "component-did-update")("Component Did Update: "),
        <.input(^.id := "component-did-update", ^.`type`.checkbox, ^.checked := context.state.componentDidUpdateCalled)()
      )
  )
}

object Main extends JSApp {
  override def main(): Unit = {
    val mountNode = dom.document.getElementById("mount-node")
    ReactDOM.render(<(App()).empty, mountNode)
  }
}
