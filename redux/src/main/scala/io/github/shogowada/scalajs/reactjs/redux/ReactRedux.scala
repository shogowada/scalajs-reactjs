package io.github.shogowada.scalajs.reactjs.redux

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.classes.specs.ReactClassSpec
import io.github.shogowada.scalajs.reactjs.classes.specs.ReactClassSpec.Render
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.redux.Redux.NativeDispatch
import io.github.shogowada.statictags.Element

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object ContainerComponent {
  def ownPropsFromNative[OwnProps](nativeOwnProps: js.Dynamic): OwnProps =
    nativeOwnProps.wrapped.asInstanceOf[OwnProps]

  def ownPropsToNative[OwnProps](ownProps: OwnProps): js.Dynamic =
    js.Dynamic.literal("wrapped" -> ownProps.asInstanceOf[js.Any])
}

class ContainerComponent(containerComponent: ReactClass) {
  def apply(attributes: Any*)(children: Any*): ReactElement = {
    React.createElement(
      containerComponent,
      VirtualDOMAttributes.toReactAttributes(Element.flattenAttributes(attributes)),
      VirtualDOMElements.toReactElements(Element.flattenContents(children)): _*
    )
  }

  def empty = this.apply()()
}

class ContainerComponentFactory[WrappedProps](nativeFactory: js.Function1[js.Any, ReactClass]) {
  def apply[State](classSpec: ReactClassSpec[WrappedProps, State]): ContainerComponent =
    this.apply(React.createClass(classSpec))

  def apply[State](reactClass: ReactClass): ContainerComponent = {
    val nativeContainerComponent: ReactClass = nativeFactory(reactClass)
    new ContainerComponent(nativeContainerComponent)
  }

  def apply(render: Render[WrappedProps]): ContainerComponent = {
    val nativeRender = ReactClassSpec.renderToNative(render)
    val nativeContainerComponent: ReactClass = nativeFactory(nativeRender)
    new ContainerComponent(nativeContainerComponent)
  }
}

/** Facade for react-redux */
object ReactRedux {

  import Redux.Dispatch

  /** [[io.github.shogowada.scalajs.reactjs.VirtualDOM]] extension for react-redux components */
  implicit class RichVirtualDOMElements(elements: VirtualDOMElements) {
    def Provider(store: Store)(child: ReactElement): ReactElement = {
      val props = js.Dynamic.literal(
        "store" -> store
      )
      React.createElement(NativeReactReduxProvider, props, child)
    }
  }

  def connectAdvanced[ReduxState, OwnProps, WrappedProps](
      selectorFactory: Dispatch => (ReduxState, OwnProps) => WrappedProps
  ): ContainerComponentFactory[WrappedProps] = {
    val nativeSelectorFactory = selectorFactoryToNative(selectorFactory)

    val nativeFactory = NativeReactRedux.connectAdvanced(nativeSelectorFactory)
    new ContainerComponentFactory[WrappedProps](nativeFactory)
  }

  private def selectorFactoryToNative[ReduxState, OwnProps, WrappedProps](
      selectorFactory: Dispatch => (ReduxState, OwnProps) => WrappedProps
  ): js.Function1[NativeDispatch, js.Function2[ReduxState, js.Dynamic, js.Any]] =
    (nativeDispatch: NativeDispatch) => {
      val dispatch: Dispatch = dispatchFromNative(nativeDispatch)
      val selector = selectorFactory(dispatch)
      selectorToNative(selector)
    }

  private def selectorToNative[ReduxState, OwnProps, WrappedProps](
      selector: (ReduxState, OwnProps) => WrappedProps
  ): js.Function2[ReduxState, js.Dynamic, js.Any] =
    (state: ReduxState, nativeOwnProps: js.Dynamic) => {
      val ownProps: OwnProps = ContainerComponent.ownPropsFromNative(nativeOwnProps)
      val wrappedProps: WrappedProps = selector(state, ownProps)
      val nativeProps = clone(nativeOwnProps)
      nativeProps.updateDynamic(ReactClassSpec.WrappedProperty)(wrappedProps.asInstanceOf[js.Any])
      nativeProps
    }

  private def clone(plainObject: js.Dynamic): js.Dynamic = {
    val clonedPlainObject = js.Dynamic.literal()
    val keys = js.Object.keys(plainObject.asInstanceOf[js.Object])
    keys.foreach(key => clonedPlainObject.updateDynamic(key)(plainObject.selectDynamic(key)))
    clonedPlainObject
  }

  private def dispatchFromNative(nativeDispatch: NativeDispatch): Dispatch =
    (action: Action) => {
      val nativeAction = Action.actionToNative(action)
      Action.actionFromNative(nativeDispatch(nativeAction))
    }
}

@js.native
@JSImport("react-redux", "Provider")
object NativeReactReduxProvider extends ReactClass

@js.native
@JSImport("react-redux", JSImport.Namespace)
object NativeReactRedux extends js.Object {
  def connectAdvanced[State](selectorFactory: js.Function1[Redux.NativeDispatch, js.Function2[State, js.Dynamic, js.Any]]): js.Function1[js.Any, ReactClass] = js.native
}
