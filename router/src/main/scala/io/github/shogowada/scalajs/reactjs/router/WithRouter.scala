package io.github.shogowada.scalajs.reactjs.router

import io.github.shogowada.scalajs.reactjs.classes.ReactClass

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object WithRouter {
  def apply(reactClass: ReactClass): ReactClass = NativeWithRouter(reactClass)
}

@js.native
@JSImport("react-router", "withRouter")
object NativeWithRouter extends js.Object {
  def apply(reactClass: ReactClass): ReactClass = js.native
}
