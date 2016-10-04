package io.github.shogowada.scalajs.reactjs

import org.scalajs.dom.raw.HTMLElement

trait AbstractThis {
  type Props
  type State

  def props: Props

  def state: State

  def refs(key: String): HTMLElement

  def setState(state: State): Nothing
}
