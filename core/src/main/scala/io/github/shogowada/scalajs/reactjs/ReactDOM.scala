package io.github.shogowada.scalajs.reactjs

import io.github.shogowada.scalajs.reactjs.classes.specs.ReactClassSpec
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

object ReactDOM {
  def render(reactClassSpec: ReactClassSpec, node: dom.Node): Unit = {
    val reactClass = React.createClass(reactClassSpec)
    render(React.createElement(reactClass), node)
  }

  def render(reactClassSpec: ReactClassSpec, props: ReactClassSpec#Props, node: dom.Node): Unit = {
    val reactClass = React.createClass(reactClassSpec)
    render(React.createElement(reactClass, props), node)
  }

  def render(element: ReactElement, node: dom.Node): Unit = {
    NativeReactDOM.render(element, node)
  }
}

@js.native
@JSName("ReactDOM")
object NativeReactDOM extends js.Object {

  def render(element: ReactElement, node: dom.Node): Unit = js.native
}
