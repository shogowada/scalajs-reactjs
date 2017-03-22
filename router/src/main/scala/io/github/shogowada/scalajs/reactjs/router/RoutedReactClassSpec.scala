package io.github.shogowada.scalajs.reactjs.router

import io.github.shogowada.scalajs.reactjs.classes.specs.ReactClassSpec

import scala.scalajs.js

trait RoutedReactClassSpec[Params <: js.Any] {
  def params: Params = this.asInstanceOf[ReactClassSpec[_, _]]
      .nativeThis.props.params.asInstanceOf[Params]
}
