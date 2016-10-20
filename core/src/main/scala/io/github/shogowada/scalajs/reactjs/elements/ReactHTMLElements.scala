package io.github.shogowada.scalajs.reactjs.elements

import org.scalajs.dom.raw.HTMLElement

import scala.scalajs.js

@js.native
trait ReactHTMLElement extends HTMLElement

@js.native
trait ReactHTMLInputElement extends ReactHTMLElement {
  def value: String = js.native
}

@js.native
trait ReactHTMLCheckBoxElement extends ReactHTMLElement {
  def checked: Boolean = js.native
}

@js.native
trait ReactHTMLRadioElement extends ReactHTMLElement {
  def checked: Boolean = js.native
}

@js.native
trait ReactHTMLTextAreaElement extends ReactHTMLElement {
  def value: String = js.native
}

@js.native
trait ReactHTMLOptionElement extends ReactHTMLElement {
  def selected: Boolean = js.native
}
