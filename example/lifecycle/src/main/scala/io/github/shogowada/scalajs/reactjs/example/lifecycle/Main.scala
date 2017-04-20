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

  type Self = React.Self[Unit, State]

  def apply() = reactClass

  private lazy val reactClass = React.createClass[Unit, State](
    componentWillMount = (self) => println("componentWillMount()"),
    componentDidMount = (self) => {
      println("componentDidMount()")
      self.setState(_.copy(componentDidMountCalled = true))
    },
    shouldComponentUpdate = (self, nextProps, nextState) => {
      println(s"shouldComponentUpdate($nextProps, $nextState)")
      nextState != self.state
    },
    componentWillReceiveProps = (self, nextProps) => {
      println(s"componentWillReceiveProps($nextProps)")
    },
    componentWillUpdate = (self, nextProps, nextState) => {
      println(s"componentWillUpdate($nextProps, $nextState)")
    },
    componentDidUpdate = (self, prevProps, prevState) => {
      println(s"componentDidUpdate($prevProps, $prevState)")
      self.setState(_.copy(componentDidUpdateCalled = true))
    },
    componentWillUnmount = (self) => {
      println("componentWillUnmount()")
    },
    getInitialState = (self) => State(
      componentDidMountCalled = false,
      componentDidUpdateCalled = false
    ),
    render = (self) =>
      <.div()(
        <.label(^.`for` := "component-did-mount")("Component Did Mount: "),
        <.input(^.id := "component-did-mount", ^.`type`.checkbox, ^.checked := self.state.componentDidMountCalled)(),
        <.label(^.`for` := "component-did-update")("Component Did Update: "),
        <.input(^.id := "component-did-update", ^.`type`.checkbox, ^.checked := self.state.componentDidUpdateCalled)()
      )
  )
}

object Main extends JSApp {
  override def main(): Unit = {
    val mountNode = dom.document.getElementById("mount-node")
    ReactDOM.render(<(App()).empty, mountNode)
  }
}
