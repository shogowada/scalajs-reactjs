package io.github.shogowada.scalajs.reactjs.events

import io.github.shogowada.scalajs.reactjs.elements._

import scala.scalajs.js

@js.native
trait SyntheticEvent extends js.Object {

  val bubbles: Boolean = js.native
  val cancelable: Boolean = js.native
  val defaultPrevented: Boolean = js.native
  val eventPhase: Int = js.native
  val isTrusted: Boolean = js.native

  def preventDefault(): Unit = js.native

  def isDefaultPrevented(): Boolean = js.native

  def stopPropagation(): Unit = js.native

  def isPropagationStopped(): Boolean = js.native

  val timeStamp: Long = js.native
  val `type`: String = js.native
}

@js.native
trait ElementSyntheticEvent[ELEMENT <: ReactHTMLElement] extends SyntheticEvent {
  val target: ReactHTMLInputElement = js.native
}

@js.native
trait CheckBoxElementSyntheticEvent extends ElementSyntheticEvent[ReactHTMLCheckBoxElement]

@js.native
trait InputElementSyntheticEvent extends ElementSyntheticEvent[ReactHTMLInputElement]

@js.native
trait OptionElementSyntheticEvent extends ElementSyntheticEvent[ReactHTMLOptionElement]

@js.native
trait RadioElementSyntheticEvent extends ElementSyntheticEvent[ReactHTMLRadioElement]

@js.native
trait TextAreaElementSyntheticEvent extends ElementSyntheticEvent[ReactHTMLTextAreaElement]
