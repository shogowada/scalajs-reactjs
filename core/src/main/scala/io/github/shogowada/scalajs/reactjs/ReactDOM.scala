package io.github.shogowada.scalajs.reactjs

import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("react-dom", JSImport.Namespace)
object ReactDOM extends js.Object {
  def render(element: ReactElement, node: dom.Node): js.Any = js.native
}
