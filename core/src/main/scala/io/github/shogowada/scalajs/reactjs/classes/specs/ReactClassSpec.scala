package io.github.shogowada.scalajs.reactjs.classes.specs

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.elements.ReactElement

import scala.scalajs.js

trait ReactClassSpec[Props, State] {

  def propsToRawJs(value: Props): js.Any = js.Dynamic.literal("wrapped" -> value.asInstanceOf[js.Any])

  def rawJsToProps(value: js.Any): Props = value.asInstanceOf[js.Dynamic].wrapped.asInstanceOf[Props]

  def stateToRawJs(value: State): js.Any = js.Dynamic.literal("wrapped" -> value.asInstanceOf[js.Any])

  def rawJsToState(value: js.Any): State = value.asInstanceOf[js.Dynamic].wrapped.asInstanceOf[State]

  def props: Props = rawJsToProps(nativeThis.props)

  def state: State = rawJsToState(nativeThis.state)

  def children: js.Any = nativeThis.props.children

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

  def apply(props: Props)(children: js.Any*): ReactElement = React.createElement(this, props, children: _*)

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

trait StatelessReactClassSpec[Props] extends ReactClassSpec[Props, Unit] {
  override def getInitialState(): Unit = ()
}

trait PropslessReactClassSpec[State] extends ReactClassSpec[Unit, State] {
  def apply(children: js.Any*): ReactElement =
    this.asInstanceOf[ReactClassSpec[Unit, State]]
        .apply(())(children: _*)
}

trait StaticReactClassSpec extends ReactClassSpec[Unit, Unit] {
  override def getInitialState(): Unit = ()

  def apply(children: js.Any*): ReactElement =
    this.asInstanceOf[ReactClassSpec[Unit, Unit]]
        .apply(())(children: _*)
}
