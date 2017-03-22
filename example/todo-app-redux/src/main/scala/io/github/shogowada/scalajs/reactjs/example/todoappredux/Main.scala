package io.github.shogowada.scalajs.reactjs.example.todoappredux

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux._
import io.github.shogowada.scalajs.reactjs.redux.Redux
import org.scalajs.dom

import scala.scalajs.js.JSApp

object Main extends JSApp {
  override def main(): Unit = {
    val mountNode = dom.document.getElementById("mount-node")

    // Use Redux.createStore(reducer) to create a store.
    val store = Redux.createStore(Reducer.reduce)

    /*
    * Use <.Provider(store)(children) to create a virtual DOM for your Redux containers.
    * Note that you need to import the following to access the Provider
    * - import io.github.shogowada.scalajs.reactjs.VirtualDOM._
    * - import io.github.shogowada.scalajs.reactjs.redux.ReactRedux._
    * */
    ReactDOM.render(
      <.Provider(store = store)(
        App()
      ),
      mountNode
    )
  }
}
