package io.github.shogowada.scalajs.reactjs.events

import io.github.shogowada.scalajs.reactjs.elements.ReactHTMLInputElement

import scala.scalajs.js

@js.native
trait SyntheticEvent extends js.Object {

  def bubbles: Boolean = js.native

  def cancelable: Boolean = js.native

  def defaultPrevented: Boolean = js.native

  def eventPhase: Int = js.native

  def isTrusted: Boolean = js.native

  def preventDefault(): Unit = js.native

  def isDefaultPrevented(): Boolean = js.native

  def stopPropagation(): Unit = js.native

  def isPropagationStopped(): Boolean = js.native

  def timeStamp: Long = js.native

  def `type`: String = js.native
}

@js.native
trait InputElementSyntheticEvent extends SyntheticEvent {
  def target: ReactHTMLInputElement = js.native
}
