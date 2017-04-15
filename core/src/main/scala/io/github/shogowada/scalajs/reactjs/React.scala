package io.github.shogowada.scalajs.reactjs

import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.classes.specs.ReactClassSpec
import io.github.shogowada.scalajs.reactjs.elements.ReactElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

/** Facade for react */
object React {

  /** Returns [[ReactClass]] created by given [[ReactClassSpec]] */
  def createClass[Props, State](spec: ReactClassSpec[Props, State]): ReactClass =
    NativeCreateReactClass(spec.asNative)

  /** Returns [[ReactElement]] created by given tag name, attributes, and children */
  def createElement(tagName: String, attributes: js.Any, children: js.Any*): ReactElement =
    NativeReact.createElement(tagName, attributes, children: _*)

  /** Returns [[ReactElement]] created by given [[ReactClass]], its props, and children */
  def createElement(reactClass: ReactClass, props: js.Any, children: js.Any*): ReactElement = {
    NativeReact.createElement(reactClass, props, children: _*)
  }

  def createElement(reactClass: ReactClass): ReactElement =
    NativeReact.createElement(reactClass)
}

@js.native
@JSImport("react", JSImport.Default)
object NativeReact extends js.Object {
  def createElement(tagName: String, attributes: js.Any, children: js.Any*): ReactElement = js.native

  def createElement(reactClass: ReactClass): ReactElement = js.native

  def createElement(reactClass: ReactClass, attributes: js.Any, children: js.Any*): ReactElement = js.native
}

@js.native
@JSImport("create-react-class", JSImport.Default)
object NativeCreateReactClass extends js.Object {
  def apply(spec: js.Any): ReactClass = js.native
}
