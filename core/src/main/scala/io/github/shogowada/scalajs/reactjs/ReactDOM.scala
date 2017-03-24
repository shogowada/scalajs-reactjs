package io.github.shogowada.scalajs.reactjs

import io.github.shogowada.scalajs.reactjs.classes.specs.ReactClassSpec
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

/** Facade for react-dom */
object ReactDOM {
  /** Mounts [[ReactClassSpec]] to given node */
  def render[Props, State](reactClassSpec: ReactClassSpec[Props, State], node: dom.Node): Unit = {
    render(React.createElement(reactClassSpec), node)
  }

  /** Mounts [[ReactClassSpec]] with its props to given node */
  def render[Props, State](reactClassSpec: ReactClassSpec[Props, State], props: Props, node: dom.Node): Unit = {
    render(React.createElement(reactClassSpec, props), node)
  }

  /** Mounts [[ReactElement]] to given node */
  def render(element: ReactElement, node: dom.Node): Unit = {
    NativeReactDOM.render(element, node)
  }
}

@js.native
@JSImport("react-dom", JSImport.Namespace)
object NativeReactDOM extends js.Object {

  def render(element: ReactElement, node: dom.Node): Unit = js.native
}
