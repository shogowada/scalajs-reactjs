package io.github.shogowada.scalajs.reactjs.redux.devtools

import io.github.shogowada.scalajs.reactjs.redux.Redux.NativeEnhancer

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("redux-devtools-extension", JSImport.Namespace)
object ReduxDevTools extends js.Object {
  def devToolsEnhancer(): NativeEnhancer = js.native
}
