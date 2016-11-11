package io.github.shogowada.scalajs.reactjs.router

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
trait History extends js.Object

@js.native
@JSImport("react-router", "browserHistory")
object BrowserHistory extends History

@js.native
@JSImport("react-router", "hashHistory")
object HashHistory extends History
