package io.github.shogowada.scalajs.reactjs

import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.classes.specs.ReactClassSpec
import io.github.shogowada.scalajs.reactjs.elements.ReactElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object React {

  def createClass(spec: ReactClassSpec): ReactClass =
    NativeReact.createClass(spec.asNative)

  def createElement(tagName: String, attributes: js.Any, children: js.Any*): ReactElement =
    NativeReact.createElement(tagName, attributes, children: _*)

  def createElement(spec: ReactClassSpec): ReactElement = {
    val reactClass = createClass(spec)
    NativeReact.createElement(reactClass)
  }

  def createElement(spec: ReactClassSpec, props: ReactClassSpec#Props): ReactElement = {
    val reactClass = createClass(spec)
    NativeReact.createElement(reactClass, spec.propsToRawJs(props.asInstanceOf[spec.Props]))
  }

  def createElement(spec: ReactClassSpec, props: ReactClassSpec#Props, children: js.Any*): ReactElement = {
    val reactClass = createClass(spec)
    NativeReact.createElement(reactClass, spec.propsToRawJs(props.asInstanceOf[spec.Props]), children: _*)
  }

  def createElement(reactClass: ReactClass, props: Any, children: js.Any*): ReactElement = {
    NativeReact.createElement(reactClass, props.asInstanceOf[js.Any], children: _*)
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
