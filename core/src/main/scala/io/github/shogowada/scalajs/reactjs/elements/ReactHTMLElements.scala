package io.github.shogowada.scalajs.reactjs.elements

import org.scalajs.dom.raw.HTMLElement

import scala.scalajs.js

@js.native
trait ReactHTMLElement extends HTMLElement

@js.native
trait ReactHTMLInputElement extends ReactHTMLElement {
  var value: String = js.native
}

@js.native
trait ReactHTMLCheckBoxElement extends ReactHTMLElement {
  var checked: Boolean = js.native
}

@js.native
trait ReactHTMLRadioElement extends ReactHTMLElement {
  var checked: Boolean = js.native
}

@js.native
trait ReactHTMLTextAreaElement extends ReactHTMLElement {
  var value: String = js.native
}

@js.native
trait ReactHTMLOptionElement extends ReactHTMLElement {
  var selected: Boolean = js.native
}
