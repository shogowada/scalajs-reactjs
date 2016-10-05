package io.github.shogowada.scalajs.reactjs

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

object React {

  def createClass(spec: ReactClassSpec): ReactClass =
    NativeReact.createClass(spec.toNative)

  def createElement(tagName: String, attributes: Object, content: js.Any*): ReactElement =
    NativeReact.createElement(tagName, attributes, content)

  def createElement(reactClass: ReactClass, attributes: Object): ReactElement =
    NativeReact.createElement(reactClass, attributes)
}

@js.native
@JSName("React")
object NativeReact extends js.Object {
  def createClass(spec: js.Object): ReactClass = js.native

  def createElement(tagName: String, attributes: Object, content: js.Any*): ReactElement = js.native

  def createElement(reactClass: ReactClass, attributes: Object): ReactElement = js.native
}
