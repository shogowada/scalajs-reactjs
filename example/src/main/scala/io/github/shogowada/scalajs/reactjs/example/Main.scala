package io.github.shogowada.scalajs.reactjs.example

import io.github.shogowada.scalajs.reactjs.{NativeReact, React, ReactClassThis, ReactDOM}
import org.scalajs.dom

import scala.scalajs.js.annotation.JSExport

@JSExport
object Main {
  @JSExport
  def main() = {
    val mainClass = React.createClass[String, Unit](
      render = (($: ReactClassThis[String, Unit]) => React.createElement("div", null,
        React.createElement("div", null, s"Hello, ${$.props}!")
      ))
    )
    ReactDOM.render(NativeReact.createElement(mainClass, "World"), dom.document.body)
  }
}
