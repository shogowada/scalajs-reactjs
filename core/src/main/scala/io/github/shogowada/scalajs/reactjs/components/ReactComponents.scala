package io.github.shogowada.scalajs.reactjs.components

import org.scalajs.dom.raw.HTMLElement

import scala.scalajs.js

@js.native
trait ReactInputComponent extends HTMLElement {
  def value: String = js.native
}

@js.native
trait ReactCheckBoxComponent extends HTMLElement {
  def checked: Boolean = js.native
}

@js.native
trait ReactRadioComponent extends HTMLElement {
  def checked: Boolean = js.native
}

@js.native
trait ReactTextAreaComponent extends HTMLElement {
  def value: String = js.native
}

@js.native
trait ReactOptionComponent extends HTMLElement {
  def selected: Boolean = js.native
}
