package io.github.shogowada.scalajs.reactjs

import org.scalajs.dom

import scala.scalajs.js

@js.native
object ReactDOM extends js.Object {

  def render(reactClass: ReactClass, node: dom.Node): Unit = js.native

  def render(element: ReactElement, node: dom.Node): Unit = js.native
}
