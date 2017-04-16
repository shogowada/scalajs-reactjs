package io.github.shogowada.scalajs.reactjs.redux

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.classes.specs.ReactClassSpec
import io.github.shogowada.scalajs.reactjs.classes.specs.ReactClassSpec.Render

import scala.scalajs.js

class ContainerComponentFactory[WrappedProps](nativeFactory: js.Function1[js.Any, ReactClass]) {
  def apply[State](classSpec: ReactClassSpec[WrappedProps, State]): ReactClass =
    this.apply(React.createClass(classSpec))

  def apply(reactClass: ReactClass): ReactClass = nativeFactory(reactClass)

  def apply(render: Render[WrappedProps]): ReactClass = {
    val nativeRender = ReactClassSpec.renderToNative(render)
    nativeFactory(nativeRender)
  }
}
