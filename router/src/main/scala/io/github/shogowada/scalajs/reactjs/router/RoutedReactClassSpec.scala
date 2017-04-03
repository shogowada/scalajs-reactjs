package io.github.shogowada.scalajs.reactjs.router

import io.github.shogowada.scalajs.reactjs.classes.specs.ReactClassSpec
import io.github.shogowada.scalajs.reactjs.elements.ReactElement

import scala.scalajs.js

object RoutedReactClassSpec {
  def nativeProps[Params <: js.Object](classSpec: RoutedReactClassSpec[Params]): js.Dynamic =
    classSpec.asInstanceOf[ReactClassSpec[_, _]].nativeProps
}

import io.github.shogowada.scalajs.reactjs.router.RoutedReactClassSpec._

/** [[ReactClassSpec]] extension for routed components */
trait RoutedReactClassSpec[Params <: js.Object] {

  def router: Router = new Router(nativeProps(this).router.asInstanceOf[NativeRouter])
  def location: Location = nativeProps(this).location.asInstanceOf[Location]
  def route: ReactElement = nativeProps(this).route.asInstanceOf[ReactElement]
  def routes: js.Array[ReactElement] = nativeProps(this).routes.asInstanceOf[js.Array[ReactElement]]

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
  def params: Params = nativeProps(this).params.asInstanceOf[Params]
}
