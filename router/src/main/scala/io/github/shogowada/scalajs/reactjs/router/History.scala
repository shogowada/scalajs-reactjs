package io.github.shogowada.scalajs.reactjs.router

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
trait History extends js.Object {
  def push(url: String): Unit = js.native
  def replace(url: String): Unit = js.native

  def go(delta: Int): Unit = js.native
  def goBack(): Unit = js.native
  def goForward(): Unit = js.native
}

@js.native
@JSImport("react-router", "browserHistory")
object BrowserHistory extends History

@js.native
@JSImport("react-router", "hashHistory")
object HashHistory extends History
