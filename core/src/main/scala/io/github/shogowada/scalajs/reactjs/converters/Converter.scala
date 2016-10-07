package io.github.shogowada.scalajs.reactjs.converters

import scala.scalajs.js

object Converter {

  def toJs[T](value: T): js.Any = js.Dynamic.literal("value" -> value.asInstanceOf[js.Any])

  def toScala[T](pickled: js.Any) = pickled.asInstanceOf[js.Dynamic].value.asInstanceOf[T]

  def toJs[R](function: () => R): js.Function0[R] = {
    val jsFunction: js.Function0[R] = function
    jsFunction
  }

  def toJs[T0, R](function: T0 => R): js.Function1[T0, R] = {
    val jsFunction: js.Function1[T0, R] = function
    jsFunction
  }
}
