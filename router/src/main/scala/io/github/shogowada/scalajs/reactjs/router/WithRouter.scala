package io.github.shogowada.scalajs.reactjs.router

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.classes.specs.ReactClassSpec

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object WithRouter {
  def apply[Props, State](reactClassSpec: ReactClassSpec[Props, State]): ReactClass = {
    val reactClass = React.createClass(reactClassSpec)
    NativeWithRouter(reactClass)
  }

  def apply(reactClass: ReactClass): ReactClass = NativeWithRouter(reactClass)
}

@js.native
@JSImport("react-router", "withRouter")
object NativeWithRouter extends js.Object {
  def apply(reactClass: ReactClass): ReactClass = js.native
}
