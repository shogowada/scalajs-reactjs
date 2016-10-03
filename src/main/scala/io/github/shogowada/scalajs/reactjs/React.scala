package io.github.shogowada.scalajs.reactjs

import scala.scalajs.js
import scala.scalajs.js.ThisFunction0
import scala.scalajs.js.annotation.JSName

object React {
  def createClass[PROPS, STATE](render: ThisFunction0[ReactClassThis[PROPS, STATE], ReactElement] = ((_: ReactClassThis[PROPS, STATE]) => NativeReact.createElement("div", null))): ReactClass[PROPS, STATE] =
    NativeReact.createClass(new ReactClassSpec[PROPS, STATE](
      render = render
    ))
}
