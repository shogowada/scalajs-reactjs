package io.github.shogowada.scalajs.reactjs

import scala.scalajs.js
import scala.scalajs.js.ThisFunction0

@js.native
class ReactClassSpec[PROPS, STATE](render: ThisFunction0[ReactClassThis[PROPS, STATE], ReactElement]) extends js.Object

object ReactClassSpec {
  def apply[PROPS, STATE](render: ThisFunction0[ReactClassThis[PROPS, STATE], ReactElement]) =
    new ReactClassSpec[PROPS, STATE](render = render)
}
