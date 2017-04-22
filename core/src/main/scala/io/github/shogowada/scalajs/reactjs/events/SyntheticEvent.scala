package io.github.shogowada.scalajs.reactjs.events

import org.scalajs.dom.raw.HTMLElement
import org.scalajs.dom.{DataTransfer, EventTarget, TouchList}

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
trait AnimationSyntheticEvent extends SyntheticEvent {
  val animationName: String = js.native
  val pseudoElement: String = js.native
  val elapsedTime: Float = js.native
}

@js.native
trait ClipboardSyntheticEvent extends SyntheticEvent {
  val clipboardData: DataTransfer = js.native
}

@js.native
trait CompositionSyntheticEvent extends SyntheticEvent {
  val data: String = js.native
}

@js.native
trait FocusSyntheticEvent extends SyntheticEvent {
  val relatedTarget: EventTarget = js.native
}

@js.native
trait FormSyntheticEvent[Element <: HTMLElement] extends SyntheticEvent {
  val target: Element = js.native
}

@js.native
trait ImageSyntheticEvent extends SyntheticEvent

@js.native
trait KeyboardSyntheticEvent extends SyntheticEvent {
  val altKey: Boolean = js.native
  val charCode: Int = js.native
  val ctrlKey: Boolean = js.native

  def getModifierState(key: String): Boolean = js.native

  val key: String = js.native
  val keyCode: Int = js.native
  val locale: String = js.native
  val location: Int = js.native
  val metaKey: Boolean = js.native
  val repeat: Boolean = js.native
  val shiftKey: Boolean = js.native
  val which: Int = js.native
}

@js.native
trait MediaSyntheticEvent extends SyntheticEvent

@js.native
trait MouseSyntheticEvent extends SyntheticEvent {
  val altKey: Boolean = js.native
  val button: Int = js.native
  val buttons: Int = js.native
  val clientX: Int = js.native
  val clientY: Int = js.native
  val ctrlKey: Boolean = js.native

  def getModifierState(key: String): Boolean = js.native

  val metaKey: Boolean = js.native
  val pageX: Int = js.native
  val pageY: Int = js.native
  val relatedTarget: EventTarget = js.native
  val screenX: Int = js.native
  val screenY: Int = js.native
  val shiftKey: Boolean = js.native
}

@js.native
trait SelectionSyntheticEvent extends SyntheticEvent

@js.native
trait TouchSyntheticEvent extends SyntheticEvent {
  val altKey: Boolean = js.native
  val changedTouches: TouchList = js.native
  val ctrlKey: Boolean = js.native

  def getModifierState(key: String): Boolean = js.native

  val metaKey: Boolean = js.native
  val shiftKey: Boolean = js.native
  val targetTouches: TouchList = js.native
  val touches: TouchList = js.native
}

@js.native
trait TransitionSyntheticEvent extends SyntheticEvent {
  val propertyName: String = js.native
  val pseudoElement: String = js.native
  val elapsedTime: Float = js.native
}

@js.native
trait UISyntheticEvent extends SyntheticEvent {
  val detail: Int = js.native
  // TODO: What should be the type of AbstractView?
  // val view: AbstractView
}

@js.native
trait WheelSyntheticEvent extends SyntheticEvent {
  val deltaMode: Int = js.native
  val deltaX: Int = js.native
  val deltaY: Int = js.native
  val deltaZ: Int = js.native
}
