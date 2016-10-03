package io.github.shogowada.scalajs.reactjs

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

@js.native
@JSName("React")
object NativeReact extends js.Object {
  def createClass[PROPS, STATE](reactClassSpec: ReactClassSpec[PROPS, STATE]): ReactClass[PROPS, STATE] = js.native

  def createElement(tagName: String, attributes: Object, content: js.Any*): ReactElement = js.native

  def createElement[PROPS, STATE](reactClass: ReactClass[PROPS, STATE], attributes: Object): ReactElement = js.native
}
