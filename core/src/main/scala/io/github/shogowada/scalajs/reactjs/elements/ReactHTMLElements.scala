package io.github.shogowada.scalajs.reactjs.elements

import org.scalajs.dom.raw.HTMLElement

import scala.scalajs.js

/** React HTML elements
  *
  * Get references to those elements by using {{{^.ref}}} attribute.
  * {{{
  * object Foo {
  *   case class State(text: String)
  * }
  *
  * class Foo extends PropslessReactElement[Foo.State] {
  *   import Foo._
  *
  *   var inputElement: ReactHTMLInputElement = _
  *
  *   override def render(): ReactElement = <.div()(
  *     <.input(
  *       ^.ref := ((element: ReactHTMLInputElement) => inputElement = element),
  *       ^.onChange := onChange,
  *       ^.value := state.text
  *     )()
  *   )
  *
  *   val onChange = () => {
  *     setState(State(text = inputElement.value))
  *   }
  * }
  * }}}
  *
  * You can also get references to them via [[io.github.shogowada.scalajs.reactjs.events.FormSyntheticEvent]].
  * */

@js.native
trait ReactHTMLElement extends HTMLElement

@js.native
trait ReactHTMLInputElement extends ReactHTMLElement {
  var value: String = js.native
}

@js.native
trait ReactHTMLCheckBoxElement extends ReactHTMLElement {
  var checked: Boolean = js.native
}

@js.native
trait ReactHTMLRadioElement extends ReactHTMLElement {
  var checked: Boolean = js.native
}

@js.native
trait ReactHTMLTextAreaElement extends ReactHTMLElement {
  var value: String = js.native
}

@js.native
trait ReactHTMLOptionElement extends ReactHTMLElement {
  var selected: Boolean = js.native
}
