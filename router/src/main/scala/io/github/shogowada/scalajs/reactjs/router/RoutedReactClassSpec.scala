package io.github.shogowada.scalajs.reactjs.router

import io.github.shogowada.scalajs.reactjs.classes.specs.StatelessReactClassSpec

import scala.scalajs.js

@js.native
trait RoutedReactClassProps[PARAMS] extends js.Object {
  def params: PARAMS = js.native

  def children: Seq[js.Object] = js.native
}

trait RoutedReactClassSpec extends StatelessReactClassSpec {
  type Params = js.Dynamic

  override type Props = RoutedReactClassProps[Params]

}
