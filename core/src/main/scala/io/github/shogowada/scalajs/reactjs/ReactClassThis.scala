package io.github.shogowada.scalajs.reactjs

import scala.scalajs.js

@js.native
trait ReactClassThis[PROPS, STATE] extends js.Object {
  val props: PROPS = js.native

  def state: STATE = js.native

  def refs: js.Dynamic = js.native

  def setState(state: STATE): Nothing = js.native
}
