package io.github.shogowada.scalajs.reactjs.classes.specs

import io.github.shogowada.scalajs.reactjs.Converters._
import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.elements.ReactElement

import scala.scalajs.js

trait ReactClassSpec {

  type Props
  type State

  def state: State = nativeThis.state.asScala[State]

  def props: Props = nativeThis.props.asScala[Props]

  def getInitialState(): State

  def setState(state: State): Unit = nativeThis.setState(state.asJs)

  def setState(stateMapper: State => State): Unit = {
    val nativeStateMapper: js.Function1[js.Any, js.Any] =
      (state: js.Any) => stateMapper(state.asScala[State]).asJs
    nativeThis.setState(nativeStateMapper)
  }

  def render(): ReactElement

  def apply(props: Props): ReactElement = React.createElement(this, props)

  private var _nativeThis: js.Dynamic = _

  def nativeThis: js.Dynamic = _nativeThis

  def asNative = js.Dynamic.literal(
    "getInitialState" -> js.ThisFunction.fromFunction1((newNativeThis: js.Dynamic) => {
      _nativeThis = newNativeThis
      Option(getInitialState()).map(_.asJs).orNull
    }),
    "render" -> js.ThisFunction.fromFunction1((newNativeThis: js.Dynamic) => {
      _nativeThis = newNativeThis
      render()
    })
  )
}
