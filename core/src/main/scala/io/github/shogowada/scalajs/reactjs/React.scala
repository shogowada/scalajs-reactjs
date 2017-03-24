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
    NativeReact.createClass(spec.asNative)

  /** Returns [[ReactElement]] created by given tag name, attributes, and children */
  def createElement(tagName: String, attributes: js.Any, children: js.Any*): ReactElement =
    NativeReact.createElement(tagName, attributes, children: _*)

  /** Returns [[ReactElement]] created by given [[ReactClassSpec]] */
  def createElement[Props, State](spec: ReactClassSpec[Props, State]): ReactElement = {
    val reactClass = createClass(spec)
    NativeReact.createElement(reactClass)
  }

  /** Returns [[ReactElement]] created by given [[ReactClassSpec]] and its props */
  def createElement[Props, State](spec: ReactClassSpec[Props, State], props: Props): ReactElement = {
    val reactClass = createClass(spec)
    NativeReact.createElement(reactClass, ReactClassSpec.propsToNative(props))
  }

  /** Returns [[ReactElement]] created by given [[ReactClassSpec]], its props, and children */
  def createElement[Props, State](spec: ReactClassSpec[Props, State], props: Props, children: js.Any*): ReactElement = {
    val reactClass = createClass(spec)
    NativeReact.createElement(reactClass, ReactClassSpec.propsToNative(props), children: _*)
  }

  /** Returns [[ReactElement]] created by given [[ReactClass]], its props, and children */
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
