package io.github.shogowada.scalajs.reactjs.classes.specs

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.elements.ReactElement

import scala.scalajs.js

object ReactClassSpec {

  type Renderer[Props] = Props => ReactElement
  type RendererWithChildren[Props] = (Props, ReactElement) => ReactElement

  def propsToNative[Props](value: Props): js.Dynamic = js.Dynamic.literal("wrapped" -> value.asInstanceOf[js.Any])

  def propsFromNative[Props](value: js.Any): Props = value.asInstanceOf[js.Dynamic].wrapped.asInstanceOf[Props]

  def stateToNative[State](value: State): js.Dynamic = js.Dynamic.literal("wrapped" -> value.asInstanceOf[js.Any])

  def stateFromNative[State](value: js.Any): State = value.asInstanceOf[js.Dynamic].wrapped.asInstanceOf[State]
}

/** Specification for React components
  *
  * {{{
  * object Foo {
  *   case class Props(foo: String)
  *   case class State(bar: String)
  * }
  *
  * class Foo extends ReactClassSpec[Foo.Props, Foo.State] {
  *   import Foo._
  *
  *   override def getInitialState() = State("bar")
  *
  *   override def render(): ReactElement = <.div()(
  *     s"foo = ${props.foo}",
  *     s"bar = ${state.bar}",
  *     children // equivalent of props.children in native React
  *   )
  * }
  *
  * val foo = new Foo()
  * ReactDOM.render(
  *   foo(Foo.Props("foo"))( // first parameter group of apply method takes props
  *     <.div()("first child"), // second parameter group of apply method takes children
  *     <.div()("second child)
  *   ),
  *   mountNode
  * )
  * }}}
  * */
trait ReactClassSpec[Props, State] {

  def propsToNative(value: Props) = ReactClassSpec.propsToNative(value)

  def propsFromNative(value: js.Any) = ReactClassSpec.propsFromNative[Props](value)

  def stateToNative(value: State) = ReactClassSpec.stateToNative(value)

  def stateFromNative(value: js.Any) = ReactClassSpec.stateFromNative[State](value)

  def props: Props = propsFromNative(nativeThis.props)

  def state: State = stateFromNative(nativeThis.state)

  /** Returns props.children equivalent in native React */
  def children: ReactElement = nativeThis.props.children.asInstanceOf[ReactElement]

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

  def setState(state: State): Unit = nativeThis.setState(stateToNative(state))

  def setState(stateMapper: State => State): Unit = {
    val nativeStateMapper: js.Function1[js.Object, js.Any] =
      (prevState: js.Object) => stateToNative(stateMapper(stateFromNative(prevState)))
    nativeThis.setState(nativeStateMapper)
  }

  def setState(stateMapper: (State, Props) => State): Unit = {
    val nativeStateMapper: js.Function2[js.Object, js.Object, js.Any] =
      (prevState: js.Object, props: js.Object) => stateToNative(stateMapper(stateFromNative(prevState), propsFromNative(props)))
    nativeThis.setState(nativeStateMapper)
  }

  def render(): ReactElement

  /** Returns [[ReactElement]] */
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
      componentWillReceiveProps(propsFromNative(nextProps))
    }),
    "shouldComponentUpdate" -> js.ThisFunction.fromFunction3((newNativeThis: js.Dynamic, nextProps: js.Object, nextState: js.Object) => {
      _nativeThis = newNativeThis
      shouldComponentUpdate(propsFromNative(nextProps), stateFromNative(nextState))
    }),
    "componentWillUpdate" -> js.ThisFunction.fromFunction3((newNativeThis: js.Dynamic, nextProps: js.Object, nextState: js.Object) => {
      _nativeThis = newNativeThis
      componentWillUpdate(propsFromNative(nextProps), stateFromNative(nextState))
    }),
    "componentDidUpdate" -> js.ThisFunction.fromFunction3((newNativeThis: js.Dynamic, prevProps: js.Object, prevState: js.Object) => {
      _nativeThis = newNativeThis
      componentDidUpdate(propsFromNative(prevProps), stateFromNative(prevState))
    }),
    "componentWillUnmount" -> js.ThisFunction.fromFunction1((newNativeThis: js.Dynamic) => {
      _nativeThis = newNativeThis
      componentWillUnmount()
    }),
    "getInitialState" -> js.ThisFunction.fromFunction1((newNativeThis: js.Dynamic) => {
      _nativeThis = newNativeThis
      Option(getInitialState()).map(stateToNative).orNull
    }),
    "render" -> js.ThisFunction.fromFunction1((newNativeThis: js.Dynamic) => {
      _nativeThis = newNativeThis
      render()
    })
  )
}

/** [[ReactClassSpec]] without state */
trait StatelessReactClassSpec[Props] extends ReactClassSpec[Props, Unit] {
  override def getInitialState(): Unit = ()
}

/** [[ReactClassSpec]] without props */
trait PropslessReactClassSpec[State] extends ReactClassSpec[Unit, State] {
  /** Returns [[ReactElement]]
    *
    * Because [[PropslessReactClassSpec]] does not have props, it only takes one parameter group for children.
    * */
  def apply(children: js.Any*): ReactElement =
    this.asInstanceOf[ReactClassSpec[Unit, State]]
        .apply(())(children: _*)
}

/** [[ReactClassSpec]] without props and state */
trait StaticReactClassSpec extends ReactClassSpec[Unit, Unit] {
  override def getInitialState(): Unit = ()

  /** Returns [[ReactElement]]
    *
    * Because [[StaticReactClassSpec]] does not have props, it only takes one parameter group for children.
    * */
  def apply(children: js.Any*): ReactElement =
    this.asInstanceOf[ReactClassSpec[Unit, Unit]]
        .apply(())(children: _*)
}
