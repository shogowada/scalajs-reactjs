package io.github.shogowada.scalajs.reactjs.events

import io.github.shogowada.scalajs.reactjs.elements._

import scala.scalajs.js

/** [[SyntheticEvent]] for forms
  *
  * In addition to regular [[SyntheticEvent]], it as {{{target}}} attribute, which holds reference to [[ReactHTMLElement]].
  * */
@js.native
trait FormSyntheticEvent[Element <: ReactHTMLElement] extends SyntheticEvent {
  val target: Element = js.native
}

@js.native
trait CheckBoxFormSyntheticEvent extends FormSyntheticEvent[ReactHTMLCheckBoxElement]

@js.native
trait InputFormSyntheticEvent extends FormSyntheticEvent[ReactHTMLInputElement]

@js.native
trait OptionFormSyntheticEvent extends FormSyntheticEvent[ReactHTMLOptionElement]

@js.native
trait RadioFormSyntheticEvent extends FormSyntheticEvent[ReactHTMLRadioElement]

@js.native
trait TextAreaFormSyntheticEvent extends FormSyntheticEvent[ReactHTMLTextAreaElement]
