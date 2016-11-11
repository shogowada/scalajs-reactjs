package io.github.shogowada.scalajs.reactjs.classes.specs

import scala.scalajs.js

trait StatelessReactClassSpec extends ReactClassSpec {

  override type State = js.Object

  override def getInitialState(): State = null
}
