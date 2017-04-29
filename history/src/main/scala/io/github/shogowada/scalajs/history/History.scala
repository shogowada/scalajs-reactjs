package io.github.shogowada.scalajs.history

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
@JSImport("history", JSImport.Namespace)
object History extends js.Object {
  def createBrowserHistory(): History = js.native
  def createHashHistory(): History = js.native
  def createMemoryHistory(): History = js.native
}
