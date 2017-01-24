package io.github.shogowada.scalajs.reactjs.classes.specs

import io.github.shogowada.scalajs.reactjs.Converters._
import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.elements.ReactElement

import scala.scalajs.js

trait ReactClassSpec {

  type Props
  type State

  def props: Props = nativeThis.props.asScala[Props]

  def state: State = nativeThis.state.asScala[State]

  def componentWillMount(): Unit = {}

  def componentDidMount(): Unit = {}

  def componentWillReceiveProps(nextProps: Props): Unit = {}

  def shouldComponentUpdate(nextProps: Props, nextState: State): Boolean = {
    true
  }

  def componentWillUpdate(nextProps: Props, nextState: State): Unit = {}

  def componentDidUpdate(prevProps: Props, prevState: State): Unit = {}

  def componentWillUnmount(): Unit = {}

  def forceUpdate(callback: js.Function0[Unit]): Unit = nativeThis.forceUpdate(callback)

  def forceUpdate(): Unit = nativeThis.forceUpdate()

  def getInitialState(): State

  def setState(state: State): Unit = nativeThis.setState(state.asJs)

  def setState(stateMapper: State => State): Unit = {
    val nativeStateMapper: js.Function1[js.Object, js.Any] =
      (prevState: js.Object) => stateMapper(prevState.asScala[State]).asJs
    nativeThis.setState(nativeStateMapper)
  }

  def setState(stateMapper: (State, Props) => State): Unit = {
    val nativeStateMapper: js.Function2[js.Object, js.Object, js.Any] =
      (prevState: js.Object, props: js.Object) => stateMapper(prevState.asScala[State], props.asScala[Props]).asJs
    nativeThis.setState(nativeStateMapper)
  }

  def render(): ReactElement

  def apply(props: Props): ReactElement = React.createElement(this, props)

  private var _nativeThis: js.Dynamic = _

  def nativeThis: js.Dynamic = _nativeThis

  def asNative: js.Dynamic = js.Dynamic.literal(
    "componentWillMount" -> js.ThisFunction.fromFunction1((newNativeThis: js.Dynamic) => {
      _nativeThis = newNativeThis
      componentWillMount()
    }),
    "componentDidMount" -> js.ThisFunction.fromFunction1((newNativeThis: js.Dynamic) => {
      _nativeThis = newNativeThis
      componentDidMount()
    }),
    "componentWillReceiveProps" -> js.ThisFunction.fromFunction2((newNativeThis: js.Dynamic, nextProps: js.Object) => {
      _nativeThis = newNativeThis
      componentWillReceiveProps(nextProps.asScala[Props])
    }),
    "shouldComponentUpdate" -> js.ThisFunction.fromFunction3((newNativeThis: js.Dynamic, nextProps: js.Object, nextState: js.Object) => {
      _nativeThis = newNativeThis
      shouldComponentUpdate(nextProps.asScala[Props], nextState.asScala[State])
    }),
    "componentWillUpdate" -> js.ThisFunction.fromFunction3((newNativeThis: js.Dynamic, nextProps: js.Object, nextState: js.Object) => {
      _nativeThis = newNativeThis
      componentWillUpdate(nextProps.asScala[Props], nextState.asScala[State])
    }),
    "componentDidUpdate" -> js.ThisFunction.fromFunction3((newNativeThis: js.Dynamic, prevProps: js.Object, prevState: js.Object) => {
      _nativeThis = newNativeThis
      componentDidUpdate(prevProps.asScala[Props], prevState.asScala[State])
    }),
    "componentWillUnmount" -> js.ThisFunction.fromFunction1((newNativeThis: js.Dynamic) => {
      _nativeThis = newNativeThis
      componentWillUnmount()
    }),
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
