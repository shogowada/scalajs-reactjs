package io.github.shogowada.scalajs.reactjs

import scala.scalajs.js

object Converters {

  implicit class ScalaObject[T](value: T) {
    def asJs: js.Any = {
      js.Dynamic.literal("value" -> value.asInstanceOf[js.Any])
    }
  }

  implicit class JsObject(value: js.Any) {
    def asScala[T]: T = {
      value.asInstanceOf[js.Dynamic].value.asInstanceOf[T]
    }
  }

  implicit class ScalaFunction0[T](function: () => T) {
    def asJs: js.Function0[T] = {
      val jsFunction: js.Function0[T] = function
      jsFunction
    }
  }

  implicit class ScalaFunction1[T0, R](function: T0 => R) {
    def asJs: js.Function1[T0, R] = {
      val jsFunction: js.Function1[T0, R] = function
      jsFunction
    }
  }
}
