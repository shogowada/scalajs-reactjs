package io.github.shogowada.scalajs.reactjs.redux

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSImport}

trait Action {
  @JSExport("type")
  val `type`: String
}

@js.native
trait Store extends js.Object

object Redux {
  type Dispatch = Action => Unit

  def createStore[State](reducer: (State, Action) => State): Store = {
    NativeRedux.createStore(reducer)
  }
}

@js.native
@JSImport("redux", JSImport.Namespace)
object NativeRedux extends js.Object {
  def createStore[State](reducer: js.Function2[State, Action, State]): Store = js.native
}
