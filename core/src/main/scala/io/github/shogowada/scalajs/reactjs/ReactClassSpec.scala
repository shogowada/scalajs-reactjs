package io.github.shogowada.scalajs.reactjs

import org.scalajs.dom.raw.HTMLElement

import scala.scalajs.js

trait ReactClassSpec {

  class This(self: js.Dynamic) extends AbstractThis[Props, State] {
    override def props: Props = self.props.asInstanceOf[Props]

    override def state: State = self.state.asInstanceOf[State]

    override def setState(state: State): Unit = self.setState(state.asInstanceOf[js.Object])

    override def refs(key: String): HTMLElement = ???
  }

  type Props
  type State

  def render(self: This): ReactElement

  def toNative: js.Object = {
    val nativeRender: js.ThisFunction0[This, ReactElement] = (self: This) => render(self)
    js.Dynamic.literal(
      "displayName" -> getClass.getName,
      "render" -> nativeRender
    )
  }
}
