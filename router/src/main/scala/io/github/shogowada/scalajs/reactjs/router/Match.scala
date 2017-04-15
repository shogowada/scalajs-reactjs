package io.github.shogowada.scalajs.reactjs.router

import scala.scalajs.js

@js.native
trait Match extends js.Object {
  val isExact: Boolean = js.native
  val params: js.Dictionary[String] = js.native
  val path: String = js.native
  val url: String = js.native
}
