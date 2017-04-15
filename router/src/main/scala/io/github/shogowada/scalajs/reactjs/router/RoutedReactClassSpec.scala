package io.github.shogowada.scalajs.reactjs.router

import io.github.shogowada.scalajs.reactjs.classes.specs.{Props, ReactClassSpec}
import io.github.shogowada.scalajs.reactjs.elements.ReactElement

import scala.scalajs.js

/** [[ReactClassSpec]] extension for routed components */
trait RoutedReactClassSpec[Params <: js.Object] {
  implicit class RoutedReactClassProps(props: Props[_]) {
    def router: Router = new Router(props.native.router.asInstanceOf[NativeRouter])
    def location: Location = props.native.location.asInstanceOf[Location]
    def route: ReactElement = props.native.route.asInstanceOf[ReactElement]
    def routes: js.Array[ReactElement] = props.native.routes.asInstanceOf[js.Array[ReactElement]]
    def params: Params = props.native.params.asInstanceOf[Params]
  }
}
