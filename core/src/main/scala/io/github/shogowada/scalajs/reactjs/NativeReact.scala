package io.github.shogowada.scalajs.reactjs

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

@js.native
@JSName("React")
object NativeReact extends js.Object {
  def createClass(spec: ReactClassSpec): ReactClass = js.native

  def createElement(tagName: String, attributes: Object, content: js.Any*): ReactElement = js.native

  def createElement(reactClass: ReactClass, attributes: Object): ReactElement = js.native
}
