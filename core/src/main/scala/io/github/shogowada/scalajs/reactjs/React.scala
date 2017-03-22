package io.github.shogowada.scalajs.reactjs

import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.classes.specs.ReactClassSpec
import io.github.shogowada.scalajs.reactjs.elements.ReactElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object React {

  def createClass[Props, State](spec: ReactClassSpec[Props, State]): ReactClass =
    NativeReact.createClass(spec.asNative)

  def createElement(tagName: String, attributes: js.Any, children: js.Any*): ReactElement =
    NativeReact.createElement(tagName, attributes, children: _*)

  def createElement[Props, State](spec: ReactClassSpec[Props, State]): ReactElement = {
    val reactClass = createClass(spec)
    NativeReact.createElement(reactClass)
  }

  def createElement[Props, State](spec: ReactClassSpec[Props, State], props: Props): ReactElement = {
    val reactClass = createClass(spec)
    NativeReact.createElement(reactClass, spec.propsToNative(props))
  }

  def createElement[Props, State](spec: ReactClassSpec[Props, State], props: Props, children: js.Any*): ReactElement = {
    val reactClass = createClass(spec)
    NativeReact.createElement(reactClass, spec.propsToNative(props), children: _*)
  }

  def createElement(reactClass: ReactClass, props: js.Any, children: js.Any*): ReactElement = {
    NativeReact.createElement(reactClass, props, children: _*)
  }
}

@js.native
@JSImport("react", JSImport.Default)
object NativeReact extends js.Object {
  def createClass(spec: js.Any): ReactClass = js.native

  def createElement(tagName: String, attributes: js.Any, children: js.Any*): ReactElement = js.native

  def createElement(reactClass: ReactClass): ReactElement = js.native

  def createElement(reactClass: ReactClass, attributes: js.Any): ReactElement = js.native

  def createElement(reactClass: ReactClass, props: js.Any, children: js.Any*): ReactElement = js.native
}
