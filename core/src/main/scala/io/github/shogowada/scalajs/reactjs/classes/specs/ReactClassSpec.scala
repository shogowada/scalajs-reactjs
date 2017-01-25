package io.github.shogowada.scalajs.reactjs.classes.specs

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.elements.ReactElement

import scala.scalajs.js

trait ReactClassSpec {

  type Props
  type State

  val isPropsRawJs: Boolean = false
  val isStateRawJs: Boolean = false

  lazy val propsToRawJs: (Props) => js.Any = if (isPropsRawJs) {
    (value) => value.asInstanceOf[js.Any]
  } else {
    (value) => js.Dynamic.literal("value" -> value.asInstanceOf[js.Any])
  }

  lazy val rawJsToProps: (js.Any) => Props = if (isPropsRawJs) {
    (value) => value.asInstanceOf[Props]
  } else {
    (value) => value.asInstanceOf[js.Dynamic].value.asInstanceOf[Props]
  }

  lazy val stateToRawJs: (State) => js.Any = if (isStateRawJs) {
    (value) => value.asInstanceOf[js.Any]
  } else {
    (value) => js.Dynamic.literal("value" -> value.asInstanceOf[js.Any])
  }

  lazy val rawJsToState: (js.Any) => State = if (isStateRawJs) {
    (value) => value.asInstanceOf[State]
  } else {
    (value) => value.asInstanceOf[js.Dynamic].value.asInstanceOf[State]
  }

  def props: Props = rawJsToProps(nativeThis.props)

  def state: State = rawJsToState(nativeThis.state)

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

  def setState(state: State): Unit = nativeThis.setState(stateToRawJs(state))

  def setState(stateMapper: State => State): Unit = {
    val nativeStateMapper: js.Function1[js.Object, js.Any] =
      (prevState: js.Object) => stateToRawJs(stateMapper(rawJsToState(prevState)))
    nativeThis.setState(nativeStateMapper)
  }

  def setState(stateMapper: (State, Props) => State): Unit = {
    val nativeStateMapper: js.Function2[js.Object, js.Object, js.Any] =
      (prevState: js.Object, props: js.Object) => stateToRawJs(stateMapper(rawJsToState(prevState), rawJsToProps(props)))
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
      componentWillReceiveProps(rawJsToProps(nextProps))
    }),
    "shouldComponentUpdate" -> js.ThisFunction.fromFunction3((newNativeThis: js.Dynamic, nextProps: js.Object, nextState: js.Object) => {
      _nativeThis = newNativeThis
      shouldComponentUpdate(rawJsToProps(nextProps), rawJsToState(nextState))
    }),
    "componentWillUpdate" -> js.ThisFunction.fromFunction3((newNativeThis: js.Dynamic, nextProps: js.Object, nextState: js.Object) => {
      _nativeThis = newNativeThis
      componentWillUpdate(rawJsToProps(nextProps), rawJsToState(nextState))
    }),
    "componentDidUpdate" -> js.ThisFunction.fromFunction3((newNativeThis: js.Dynamic, prevProps: js.Object, prevState: js.Object) => {
      _nativeThis = newNativeThis
      componentDidUpdate(rawJsToProps(prevProps), rawJsToState(prevState))
    }),
    "componentWillUnmount" -> js.ThisFunction.fromFunction1((newNativeThis: js.Dynamic) => {
      _nativeThis = newNativeThis
      componentWillUnmount()
    }),
    "getInitialState" -> js.ThisFunction.fromFunction1((newNativeThis: js.Dynamic) => {
      _nativeThis = newNativeThis
      Option(getInitialState()).map(stateToRawJs).orNull
    }),
    "render" -> js.ThisFunction.fromFunction1((newNativeThis: js.Dynamic) => {
      _nativeThis = newNativeThis
      render()
    })
  )
}
