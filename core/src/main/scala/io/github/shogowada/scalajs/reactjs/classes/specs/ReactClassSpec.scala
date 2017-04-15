package io.github.shogowada.scalajs.reactjs.classes.specs

import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.utils.Utils

import scala.scalajs.js

case class Props[Wrapped](native: js.Dynamic) {
  def wrapped: Wrapped = native.wrapped.asInstanceOf[Wrapped]
  def children: ReactElement = native.children.asInstanceOf[ReactElement]
}

/** Specification for React components
  *
  * Example:
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
  *     <.div()("second child")
  *   ),
  *   mountNode
  * )
  * }}}
  * */
trait ReactClassSpec[WrappedProps, State] {

  def propsToNative(props: Props[WrappedProps]) = ReactClassSpec.propsToNative(props)

  def propsFromNative(nativeProps: js.Dynamic) = ReactClassSpec.propsFromNative[WrappedProps](nativeProps)

  def stateToNative(state: State) = ReactClassSpec.stateToNative(state)

  def stateFromNative(nativeState: js.Dynamic) = ReactClassSpec.stateFromNative[State](nativeState)

  def nativeProps: js.Dynamic = nativeThis.props

  def nativeState: js.Dynamic = nativeThis.state

  def props: Props[WrappedProps] = propsFromNative(nativeProps)

  def state: State = stateFromNative(nativeState)

  /** Returns props.children equivalent in native React */
  def children: ReactElement = nativeProps.children.asInstanceOf[ReactElement]

  def componentWillMount(): Unit = {}

  def componentDidMount(): Unit = {}

  def nativeComponentWillReceiveProps(nativeNextProps: js.Dynamic): Unit =
    componentWillReceiveProps(propsFromNative(nativeNextProps))

  def componentWillReceiveProps(nextProps: Props[WrappedProps]): Unit = {}

  def nativeShouldComponentUpdate(nextProps: js.Dynamic, nextState: js.Dynamic): Boolean = {
    if (shouldComponentUpdate(propsFromNative(nextProps), stateFromNative(nextState))) {
      true
    } else {
      import ReactClassSpec._
      val props = nativeThis.props
      val state = nativeThis.state

      !Utils.shallowEqual(props, nextProps, WrappedProperty) ||
          !Utils.shallowEqual(state, nextState, WrappedProperty)
    }
  }

  def shouldComponentUpdate(nextProps: Props[WrappedProps], nextState: State): Boolean =
    props.wrapped != nextProps.wrapped || state != nextState

  def nativeComponentWillUpdate(nativeNextProps: js.Dynamic, nativeNextState: js.Dynamic): Unit =
    componentWillUpdate(propsFromNative(nativeNextProps), stateFromNative(nativeNextState))

  def componentWillUpdate(nextProps: Props[WrappedProps], nextState: State): Unit = {}

  def nativeComponentDidUpdate(nativePrevProps: js.Dynamic, nativePrevState: js.Dynamic): Unit =
    componentDidUpdate(propsFromNative(nativePrevProps), stateFromNative(nativePrevState))

  def componentDidUpdate(prevProps: Props[WrappedProps], prevState: State): Unit = {}

  def componentWillUnmount(): Unit = {}

  def forceUpdate(callback: js.Function0[Unit]): Unit = nativeThis.forceUpdate(callback)

  def forceUpdate(): Unit = nativeThis.forceUpdate()

  def getInitialState(): State

  def setState(state: State): Unit = nativeThis.setState(stateToNative(state))

  def setState(stateMapper: State => State): Unit = {
    val nativeStateMapper = (prevState: js.Dynamic) => stateToNative(stateMapper(stateFromNative(prevState)))
    nativeThis.setState(nativeStateMapper)
  }

  def setState(stateMapper: (State, Props[WrappedProps]) => State): Unit = {
    val nativeStateMapper = (prevState: js.Dynamic, props: js.Dynamic) =>
      stateToNative(stateMapper(stateFromNative(prevState), propsFromNative(props)))
    nativeThis.setState(nativeStateMapper)
  }

  def render(): ReactElement

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
    "componentWillReceiveProps" -> js.ThisFunction.fromFunction2((newNativeThis: js.Dynamic, nextProps: js.Dynamic) => {
      _nativeThis = newNativeThis
      nativeComponentWillReceiveProps(nextProps)
    }),
    "shouldComponentUpdate" -> js.ThisFunction.fromFunction3((newNativeThis: js.Dynamic, nextProps: js.Dynamic, nextState: js.Dynamic) => {
      _nativeThis = newNativeThis
      nativeShouldComponentUpdate(nextProps, nextState)
    }),
    "componentWillUpdate" -> js.ThisFunction.fromFunction3((newNativeThis: js.Dynamic, nextProps: js.Dynamic, nextState: js.Dynamic) => {
      _nativeThis = newNativeThis
      nativeComponentWillUpdate(nextProps, nextState)
    }),
    "componentDidUpdate" -> js.ThisFunction.fromFunction3((newNativeThis: js.Dynamic, prevProps: js.Dynamic, prevState: js.Dynamic) => {
      _nativeThis = newNativeThis
      nativeComponentDidUpdate(prevProps, prevState)
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

object ReactClassSpec {

  type Renderer[WrappedProps] = Props[WrappedProps] => ReactElement

  def propsToNative[WrappedProps](props: Props[WrappedProps]): js.Dynamic = props.asInstanceOf[js.Dynamic]

  def propsFromNative[WrappedProps](nativeProps: js.Dynamic): Props[WrappedProps] = Props(nativeProps)

  def stateToNative[State](state: State): js.Dynamic = wrap(state)

  def stateFromNative[State](nativeState: js.Dynamic): State = unwrap[State](nativeState)

  val WrappedProperty = "wrapped"

  private def wrap[Wrapped](wrapped: Wrapped): js.Dynamic =
    js.Dynamic.literal(WrappedProperty -> wrapped.asInstanceOf[js.Any])

  private def unwrap[Wrapped](nativeWrapped: js.Dynamic): Wrapped =
    nativeWrapped.selectDynamic(WrappedProperty).asInstanceOf[Wrapped]
}

/** [[ReactClassSpec]] without state */
trait StatelessReactClassSpec[Props] extends ReactClassSpec[Props, Unit] {
  override def getInitialState(): Unit = ()
}

/** [[ReactClassSpec]] without props */
trait PropslessReactClassSpec[State] extends ReactClassSpec[Unit, State]

/** [[ReactClassSpec]] without props and state */
trait StaticReactClassSpec extends ReactClassSpec[Unit, Unit] {
  override def getInitialState(): Unit = ()
}
