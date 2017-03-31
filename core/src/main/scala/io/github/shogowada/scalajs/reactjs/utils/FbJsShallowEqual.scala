package io.github.shogowada.scalajs.reactjs.utils

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("fbjs/lib/shallowEqual", JSImport.Default)
object FbJsShallowEqual extends js.Object {
  def apply(lhs: js.Any, rhs: js.Any): Boolean = js.native
}
