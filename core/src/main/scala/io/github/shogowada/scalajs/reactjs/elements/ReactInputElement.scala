package io.github.shogowada.scalajs.reactjs.elements

import org.scalajs.dom.raw.HTMLElement

import scala.scalajs.js

@js.native
trait ReactInputElement extends HTMLElement {
  def value: String = js.native
}
