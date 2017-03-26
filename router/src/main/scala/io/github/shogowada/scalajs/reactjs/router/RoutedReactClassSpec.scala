package io.github.shogowada.scalajs.reactjs.router

import io.github.shogowada.scalajs.reactjs.classes.specs.ReactClassSpec

import scala.scalajs.js

/** [[ReactClassSpec]] extension for routed components */
trait RoutedReactClassSpec[Params <: js.Any] {
  /** props.params equivalent of native React
    *
    * {{{
    * object Foo {
    *   @js.native
    *   trait Params extends js.Object {
    *     val foo: String = js.native
    *   }
    * }
    *
    * class Foo extends StaticReactClassSpec
    *     with RoutedReactClassSpec[Foo.Params] {
    *   override def render(): ReactElement = <.div()(params.foo)
    * }
    * }}}
    * */
  def params: Params = this.asInstanceOf[ReactClassSpec[_, _]]
      .nativeProps.params.asInstanceOf[Params]
}
