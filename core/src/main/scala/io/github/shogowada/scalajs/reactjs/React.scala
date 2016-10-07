package io.github.shogowada.scalajs.reactjs

import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.classes.specs.ReactClassSpec
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.infra.Pickler

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

object React {

  def createClass(spec: ReactClassSpec): ReactClass =
    NativeReact.createClass(spec.asNative)

  def createElement(tagName: String, attributes: js.Any, content: js.Any*): ReactElement =
    NativeReact.createElement(tagName, attributes, content: _*)

  def createElement(reactClass: ReactClass): ReactElement =
    NativeReact.createElement(reactClass)

  def createElement[PROPS](reactClass: ReactClass, props: PROPS): ReactElement =
    NativeReact.createElement(reactClass, Pickler.toJs(props))
}

@js.native
@JSName("React")
object NativeReact extends js.Object {
  def createClass(spec: js.Object): ReactClass = js.native

  def createElement(tagName: String, attributes: js.Any, content: js.Any*): ReactElement = js.native

  def createElement(reactClass: ReactClass): ReactElement = js.native

  def createElement(reactClass: ReactClass, attributes: js.Any): ReactElement = js.native
}
