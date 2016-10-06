package io.github.shogowada.scalajs.reactjs

import scala.scalajs.js

trait ReactClassSpec {

  class This(self: js.Dynamic) {
  }

  def render(self: This): ReactElement

  def asNative: js.Object = {
    val nativeRender: js.ThisFunction0[js.Dynamic, ReactElement] = (self: js.Dynamic) => render(new This(self))
    js.Dynamic.literal(
      "render" -> nativeRender
    )
  }
}
