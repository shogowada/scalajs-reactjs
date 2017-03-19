package io.github.shogowada.scalajs.reactjs.router

import io.github.shogowada.scalajs.reactjs.classes.specs.{ReactClassSpec, StatelessReactClassSpec}

import scala.scalajs.js

@js.native
trait RoutedReactClassProps[Params <: js.Any] extends js.Object {
  def params: Params = js.native
}

trait RoutedReactClassSpec extends ReactClassSpec {
  type Params <: js.Any

  def params: Params = nativeThis.props.params.asInstanceOf[Params]
}

trait StatelessRoutedReactClassSpec extends RoutedReactClassSpec
    with StatelessReactClassSpec
