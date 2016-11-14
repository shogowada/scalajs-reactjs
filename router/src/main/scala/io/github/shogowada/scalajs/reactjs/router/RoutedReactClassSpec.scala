package io.github.shogowada.scalajs.reactjs.router

import io.github.shogowada.scalajs.reactjs.classes.specs.{ReactClassSpec, StatelessReactClassSpec}

import scala.scalajs.js

@js.native
trait RoutedReactClassProps[PARAMS <: js.Any] extends js.Object {
  def params: PARAMS = js.native

  def children: js.Any = js.native
}

trait RoutedReactClassSpec extends ReactClassSpec {
  type Params <: js.Any

  override type Props = RoutedReactClassProps[Params]

  override def props: Props = nativeThis.props.asInstanceOf[Props]
}

trait StatelessRoutedReactClassSpec extends RoutedReactClassSpec
    with StatelessReactClassSpec
