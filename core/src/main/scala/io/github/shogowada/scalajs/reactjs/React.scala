package io.github.shogowada.scalajs.reactjs

import io.github.shogowada.scalajs.reactjs.Converters._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.classes.specs.ReactClassSpec
import io.github.shogowada.scalajs.reactjs.elements.ReactElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

object React {

  def createClass(spec: ReactClassSpec): ReactClass =
    NativeReact.createClass(spec.asNative)

  def createElement(tagName: String, attributes: js.Any, content: js.Any*): ReactElement =
    NativeReact.createElement(tagName, attributes, content: _*)

  def createElement(spec: ReactClassSpec): ReactElement = {
    val reactClass = createClass(spec)
    NativeReact.createElement(reactClass)
  }

  def createElement(spec: ReactClassSpec, props: ReactClassSpec#Props): ReactElement = {
    val reactClass = createClass(spec)
    NativeReact.createElement(reactClass, props.asJs)
  }
}

@js.native
@JSName("React")
object NativeReact extends js.Object {
  def createClass(spec: js.Object): ReactClass = js.native

  def createElement(tagName: String, attributes: js.Any, content: js.Any*): ReactElement = js.native

  def createElement(reactClass: ReactClass): ReactElement = js.native

  def createElement(reactClass: ReactClass, attributes: js.Any): ReactElement = js.native
}
