package io.github.shogowada.scalajs.reactjs.router

import io.github.shogowada.scalajs.reactjs.classes.specs.ReactClassSpec

import scala.scalajs.js

/** [[ReactClassSpec]] extension for routed components */
trait RoutedReactClassSpec[Params <: js.Any] {
  /** props.params equivalent of native React
    *
    * {{{
    * @js.native
    * trait FooParams extends js.Object {
    *   val foo: String = js.native
    * }
    *
    * class Foo extends StaticReactClassSpec
    *     with RoutedReactClassSpec[FooParams] {
    *   override def render(): ReactElement = <.div()(params.foo)
    * }
    * }}}
    * */
  def params: Params = this.asInstanceOf[ReactClassSpec[_, _]]
      .nativeThis.props.params.asInstanceOf[Params]
}
