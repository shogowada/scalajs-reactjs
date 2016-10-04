package io.github.shogowada.scalajs.reactjs

import org.scalajs.dom.raw.HTMLElement

trait ReactClassSpec {
  type This <: AbstractThis

  def render(self: This): HTMLElement
}
