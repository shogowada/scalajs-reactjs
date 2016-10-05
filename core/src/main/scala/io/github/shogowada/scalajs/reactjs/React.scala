package io.github.shogowada.scalajs.reactjs

import scala.scalajs.js

@js.native
object React {
  def createClass(spec: ReactClassSpec): ReactClass = js.native

  def createElement(tagName: String, attributes: Object, content: js.Any*): ReactElement = js.native

  def createElement(reactClass: ReactClass, attributes: Object): ReactElement = js.native
}
