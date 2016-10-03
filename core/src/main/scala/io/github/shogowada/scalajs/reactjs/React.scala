package io.github.shogowada.scalajs.reactjs

import scala.scalajs.js
import scala.scalajs.js.ThisFunction0

object React {
  def createClass[PROPS, STATE](render: ThisFunction0[ReactClassThis[PROPS, STATE], ReactElement] = ((ignored: ReactClassThis[PROPS, STATE]) => NativeReact.createElement("div", null))): ReactClass[PROPS, STATE] =
    NativeReact.createClass(ReactClassSpec[PROPS, STATE](
      render = render
    ))

  def createElement(tagName: String, attribute: Object, content: js.Any*) =
    NativeReact.createElement(tagName, attribute, content)
}
