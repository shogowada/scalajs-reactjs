package io.github.shogowada.scalajs.reactjs

import scala.scalajs.js

object Pickler {

  def toJs[T](value: T): js.Any = js.Dynamic.literal("value" -> value.asInstanceOf[js.Any])

  def toScala[T](pickled: js.Any) = pickled.asInstanceOf[js.Dynamic].value.asInstanceOf[T]
}
