package io.github.shogowada.scalajs.reactjs.redux

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.classes.specs.ReactClassSpec
import io.github.shogowada.scalajs.reactjs.classes.specs.ReactClassSpec.Render
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.statictags.Element

import scala.scalajs.js

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
