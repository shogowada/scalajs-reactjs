package io.github.shogowada.scalajs.reactjs.example.style

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.scalajs.dom

import scala.scalajs.js.annotation.JSExport

object Main {

  @JSExport
  def main(args: Array[String]): Unit = {
    val mountNode = dom.document.createElement("div")
    dom.document.body.appendChild(mountNode)
    ReactDOM.render(Main(), mountNode)
  }

  def apply(): ReactElement = <.div()(
    <.div(^.id := "green-text", ^.className := Seq("green"))("Green text!"),
    <.div(^.id := "big-blue-text", ^.className := Seq("big", "blue"))(" Big blue text !"),
    <.div(^.id := "red-text", ^.style := Map("color" -> "red"))("Red text!")
  )
}
