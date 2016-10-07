package io.github.shogowada.scalajs.reactjs.classes.specs

import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.converters.Converter
import org.scalajs.dom.raw.HTMLElement

import scala.scalajs.js

trait ReactClassSpec {

  class This(self: js.Dynamic) {
    def state: State = Converter.toScala[State](self.state)

    def props: Props = Converter.toScala[Props](self.props)

    def refs(key: String): HTMLElement = self.refs.selectDynamic(key).asInstanceOf[HTMLElement]

    def setState(state: State): Unit = self.setState(Converter.toJs(state))
  }

  type Props
  type State

  def getInitialState(): State = ???

  def render(): ReactElement

  var self: This = _

  def asNative: js.Object = {
    val nativeGetInitialState = js.ThisFunction.fromFunction1((newSelf: js.Dynamic) => {
      self = new This(newSelf)
      Converter.toJs(getInitialState())
    })
    val nativeRender = js.ThisFunction.fromFunction1((newSelf: js.Dynamic) => {
      self = new This(newSelf)
      render()
    })
    js.Dynamic.literal(
      "getInitialState" -> nativeGetInitialState,
      "render" -> nativeRender
    )
  }
}
