package io.github.shogowada.scalajs.reactjs.redux

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Render
import io.github.shogowada.scalajs.reactjs.classes.ReactClass

import scala.scalajs.js

class ContainerComponentFactory[WrappedProps](nativeFactory: js.Function1[js.Any, ReactClass]) {
  def apply(reactClass: ReactClass): ReactClass = nativeFactory(reactClass)

  def apply(render: Render[WrappedProps]): ReactClass = {
    val nativeRender = React.renderToNative(render)
    nativeFactory(nativeRender)
  }
}
