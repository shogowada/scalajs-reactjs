package io.github.shogowada.scalajs.reactjs

import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.utils.Utils

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object React {

  type Render[WrappedProps] = Props[WrappedProps] => ReactElement

  def renderToNative[WrappedProps](render: Render[WrappedProps]): js.Function1[js.Dynamic, ReactElement] =
    (nativeProps: js.Dynamic) => render(Props(nativeProps))

  val WrappedProperty = "wrapped"

  def wrap[Wrapped](wrapped: Wrapped): js.Dynamic =
    js.Dynamic.literal(WrappedProperty -> wrapped.asInstanceOf[js.Any])

  def unwrap[Wrapped](nativeWrapped: js.Dynamic): Wrapped =
    nativeWrapped.selectDynamic(WrappedProperty).asInstanceOf[Wrapped]

  def stateToNative[State](state: State): js.Dynamic = wrap(state)

  def stateFromNative[State](nativeState: js.Dynamic): State = unwrap[State](nativeState)

  case class Props[Wrapped](native: js.Dynamic) {
    def wrapped: Wrapped = native.wrapped.asInstanceOf[Wrapped]
    def children: ReactElement = native.children.asInstanceOf[ReactElement]
  }

  case class Self[WrappedProps, State](native: js.Dynamic) {

    def props: Props[WrappedProps] = Props(native.props)
    def state: State = stateFromNative(native.state)

    def forceUpdate(callback: () => Unit): Unit = native.forceUpdate(callback)

    def forceUpdate(): Unit = native.forceUpdate()

    def setState(state: State): Unit = native.setState(stateToNative(state))

    def setState(mapper: State => State): Unit = {
      val nativeMapper = (nativeState: js.Dynamic) => {
        stateToNative(mapper(stateFromNative(nativeState)))
      }
      native.setState(nativeMapper)
    }

    def setState(mapper: (State, Props[WrappedProps]) => State): Unit = {
      val nativeMapper = (nativeState: js.Dynamic, nativeProps: js.Dynamic) => {
        stateToNative(mapper(
          stateFromNative(nativeState),
          Props(nativeProps)
        ))
      }
      native.setState(nativeMapper)
    }
  }

  def createClass[WrappedProps, State](
      render: Self[WrappedProps, State] => ReactElement,
      displayName: String = null,
      componentWillMount: Self[WrappedProps, State] => Unit = null,
      componentDidMount: Self[WrappedProps, State] => Unit = null,
      componentWillReceiveProps: (Self[WrappedProps, State], Props[WrappedProps]) => Unit = null,
      shouldComponentUpdate: (Self[WrappedProps, State], Props[WrappedProps], State) => Boolean =
      (self: Self[WrappedProps, State], nextProps: Props[WrappedProps], nextState: State) => {
        self.props.wrapped != nextProps.wrapped || self.state != nextState ||
            !Utils.shallowEqual(self.props.native, nextProps.native, WrappedProperty)
      },
      componentWillUpdate: (Self[WrappedProps, State], Props[WrappedProps], State) => Unit = null,
      componentDidUpdate: (Self[WrappedProps, State], Props[WrappedProps], State) => Unit = null,
      componentWillUnmount: Self[WrappedProps, State] => Unit = null,
      getInitialState: Self[WrappedProps, State] => State = null
  ): ReactClass = {
    type Context = React.Self[WrappedProps, State]

    val spec = js.Dynamic.literal(
      "shouldComponentUpdate" -> js.ThisFunction.fromFunction3((native: js.Dynamic, nextProps: js.Dynamic, nextState: js.Dynamic) => {
        shouldComponentUpdate(Self(native), Props(nextProps), stateFromNative(nextState))
      }),
      "getInitialState" -> js.ThisFunction.fromFunction1((native: js.Dynamic) => {
        if (getInitialState != null) {
          stateToNative(getInitialState(Self(native)))
        } else {
          stateToNative(())
        }
      }),
      "render" -> js.ThisFunction.fromFunction1((native: js.Dynamic) => {
        render(Self(native))
      })
    )

    if (displayName != null) {
      spec.updateDynamic("displayName")(displayName)
    }

    if (componentWillMount != null) {
      spec.updateDynamic("componentWillMount")(js.ThisFunction.fromFunction1((native: js.Dynamic) => {
        componentWillMount(Self(native))
      }))
    }

    if (componentDidMount != null) {
      spec.updateDynamic("componentDidMount")(js.ThisFunction.fromFunction1((native: js.Dynamic) => {
        componentDidMount(Self(native))
      }))
    }

    if (componentWillReceiveProps != null) {
      spec.updateDynamic("componentWillReceiveProps")(js.ThisFunction.fromFunction2((native: js.Dynamic, nextProps: js.Dynamic) => {
        componentWillReceiveProps(Self(native), Props(nextProps))
      }))
    }

    if (componentWillUpdate != null) {
      spec.updateDynamic("componentWillUpdate")(js.ThisFunction.fromFunction3((native: js.Dynamic, nextProps: js.Dynamic, nextState: js.Dynamic) => {
        componentWillUpdate(Self(native), Props(nextProps), stateFromNative(nextState))
      }))
    }

    if (componentDidUpdate != null) {
      spec.updateDynamic("componentDidUpdate")(js.ThisFunction.fromFunction3((native: js.Dynamic, prevProps: js.Dynamic, prevState: js.Dynamic) => {
        componentDidUpdate(Self(native), Props(prevProps), stateFromNative(prevState))
      }))
    }

    if (componentWillUnmount != null) {
      spec.updateDynamic("componentWillUnmount")(js.ThisFunction.fromFunction1((native: js.Dynamic) => {
        componentWillUnmount(Self(native))
      }))
    }

    NativeCreateReactClass(spec)
  }

  def createElement(tagName: String, attributes: js.Any, children: js.Any*): ReactElement =
    NativeReact.createElement(tagName, attributes, children: _*)

  def createElement(reactClass: ReactClass, props: js.Any, children: js.Any*): ReactElement =
    NativeReact.createElement(reactClass, props, children: _*)

  def createElement(reactClass: ReactClass): ReactElement =
    NativeReact.createElement(reactClass)
}

@js.native
@JSImport("react", JSImport.Default)
object NativeReact extends js.Object {
  def createElement(tagName: String, attributes: js.Any, children: js.Any*): ReactElement = js.native

  def createElement(reactClass: ReactClass): ReactElement = js.native

  def createElement(reactClass: ReactClass, attributes: js.Any, children: js.Any*): ReactElement = js.native
}

@js.native
@JSImport("create-react-class", JSImport.Default)
object NativeCreateReactClass extends js.Object {
  def apply(spec: js.Any): ReactClass = js.native
}
