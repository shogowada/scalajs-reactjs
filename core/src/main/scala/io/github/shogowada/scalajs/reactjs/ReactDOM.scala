package io.github.shogowada.scalajs.reactjs

import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.scalajs.dom

import scala.scalajs.js

@js.native
object ReactDOM extends js.Object {

  def render(element: ReactElement, node: dom.Node): Unit = js.native
}
