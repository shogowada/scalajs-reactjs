package io.github.shogowada.scalajs.reactjs

import org.scalajs.dom.raw.HTMLElement

import scala.scalajs.js

trait ReactClassSpec {

  class This(self: js.Dynamic) {
    def props: Props = self.props.asInstanceOf[Props]

    def state: State = self.state.asInstanceOf[State]

    def setState(state: State): Unit = self.setState(state.asInstanceOf[js.Object])

    def refs(key: String): HTMLElement = ???
  }

  type Props
  type State

  def render(self: This): ReactElement

  def asNative: js.Object = {
    val nativeRender: js.ThisFunction0[js.Dynamic, ReactElement] = (self: js.Dynamic) => render(new This(self))
    js.Dynamic.literal(
      "render" -> nativeRender
    )
  }
}
