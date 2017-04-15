package io.github.shogowada.scalajs.reactjs.example.todoappredux

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux._
import io.github.shogowada.scalajs.reactjs.redux.Redux
import org.scalajs.dom

import scala.scalajs.js.JSApp

/*
 * If you are not familiar with react-redux yet, please check it out first:
 *
 * - http://redux.js.org/docs/basics/UsageWithReact.html
 * */

object Main extends JSApp {
  override def main(): Unit = {
    val mountNode = dom.document.getElementById("mount-node")

    val store = Redux.createStore(Reducer.reduce)

    /*
     * Import the following to access the Provider:
     *
     * - import io.github.shogowada.scalajs.reactjs.VirtualDOM._
     * - import io.github.shogowada.scalajs.reactjs.redux.ReactRedux._
     * */
    ReactDOM.render(
      <.Provider(^.store := store)(
        App()
      ),
      mountNode
    )
  }
}
