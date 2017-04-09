package io.github.shogowada.scalajs.reactjs.utils

import scala.scalajs.js

object Utils {
  def shallowEqual(x: js.Any, y: js.Any, keyToIgnore: String): Boolean = {
    (x, y) match {
      case _ if js.typeOf(x) == "object" &&
          js.typeOf(y) == "object" =>
        val xObject = x.asInstanceOf[js.Dictionary[js.Any]]
        val yObject = y.asInstanceOf[js.Dictionary[js.Any]]
        val xKeys = xObject.keySet.filterNot(key => key == keyToIgnore)
        val yKeys = yObject.keySet.filterNot(key => key == keyToIgnore)
        if (xKeys != yKeys) {
          false
        } else {
          xKeys.forall(key => xObject(key) == yObject(key))
        }
      case _ => x == y
    }
  }
}
