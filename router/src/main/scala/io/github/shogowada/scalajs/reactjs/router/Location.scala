package io.github.shogowada.scalajs.reactjs.router

import scala.scalajs.js

@js.native
trait Location extends js.Object {
  val action: String = js.native
  val key: js.UndefOr[String] = js.native
  val hash: String = js.native
  val pathname: String = js.native
  val search: String = js.native
  val state: js.UndefOr[js.Dictionary[js.Any]] = js.native
}
