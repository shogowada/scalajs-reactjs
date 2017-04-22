package io.github.shogowada.scalajs.reactjs.events

import org.scalajs.dom.raw.HTMLElement

import scala.scalajs.js

@js.native
trait FormSyntheticEvent[Element <: HTMLElement] extends SyntheticEvent {
  val target: Element = js.native
}
