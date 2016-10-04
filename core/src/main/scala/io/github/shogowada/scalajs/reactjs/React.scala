package io.github.shogowada.scalajs.reactjs

import scala.scalajs.js

object React {
  def createClass(spec: ReactClassSpec) =
    NativeReact.createClass(spec)

  def createElement(tagName: String, attribute: Object, content: js.Any*) =
    NativeReact.createElement(tagName, attribute, content)
}
