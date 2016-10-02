package io.github.shogowada.scalajs.reactjs

import scala.scalajs.js

@js.native
object React {
  def createClass[PROPS](render: ((ReactClassThis[PROPS]) => String) = (_ => "")) =
    createClass(ReactClassImpl(
      render = render
    ))

  def createClass[PROPS](classImpl: ReactClassImpl[PROPS]): ReactClass[PROPS] = js.native
}
