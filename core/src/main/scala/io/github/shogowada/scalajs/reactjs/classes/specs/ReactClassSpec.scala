package io.github.shogowada.scalajs.reactjs.classes.specs

import io.github.shogowada.scalajs.reactjs.Converters._
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.scalajs.dom.raw.HTMLElement

import scala.scalajs.js

trait ReactClassSpec {

  type Props
  type State

  def state: State = nativeThis.state.asScala[State]

  def props: Props = nativeThis.props.asScala[Props]

  def getInitialState(): State = ???

  def setState(state: State): Unit = nativeThis.setState(state.asJs)

  def render(): ReactElement

  def refs(key: String): HTMLElement = nativeThis.refs.selectDynamic(key).asInstanceOf[HTMLElement]

  private var nativeThis: js.Dynamic = _

  def asNative: js.Object = {
    val nativeGetInitialState = js.ThisFunction.fromFunction1((newNativeThis: js.Dynamic) => {
      nativeThis = newNativeThis
      getInitialState().asJs
    })
    val nativeRender = js.ThisFunction.fromFunction1((newNativeThis: js.Dynamic) => {
      nativeThis = newNativeThis
      render()
    })
    js.Dynamic.literal(
      "getInitialState" -> nativeGetInitialState,
      "render" -> nativeRender
    )
  }
}
