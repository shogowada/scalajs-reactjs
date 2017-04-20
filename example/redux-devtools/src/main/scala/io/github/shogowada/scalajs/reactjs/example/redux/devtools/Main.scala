package io.github.shogowada.scalajs.reactjs.example.redux.devtools

import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.InputFormSyntheticEvent
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux._
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.redux.devtools.ReduxDevTools
import io.github.shogowada.scalajs.reactjs.redux.{Action, ReactRedux, Redux}
import io.github.shogowada.scalajs.reactjs.{React, ReactDOM}
import org.scalajs.dom

import scala.scalajs.js.JSApp

object Main extends JSApp {
  override def main(): Unit = {
    val store = Redux.createStore[State](
      Reducer(_, _),
      ReduxDevTools.devToolsEnhancer() // Just add the enhancer
    )

    val mountNode = dom.document.getElementById("mount-node")
    ReactDOM.render(
      <.Provider(^.store := store)(
        <(TextContainerComponent()).empty
      ),
      mountNode
    )
  }
}

case class State(text: String)

case class SetText(text: String) extends Action

object Reducer {
  def apply(maybeState: Option[State], action: Action): State =
    action match {
      case action: SetText => State(text = action.text)
      case _ => State(text = "")
    }
}

object TextContainerComponent {

  import TextComponent._

  def apply(): ReactClass = ReactRedux.connectAdvanced(
    (dispatch: Dispatch) => {
      val onTextChange = (text: String) => dispatch(SetText(text))
      (state: State, ownProps: Unit) => {
        WrappedProps(
          text = state.text,
          onTextChange = onTextChange
        )
      }
    }
  )(TextComponent.reactClass)
}

object TextComponent {
  case class WrappedProps(text: String, onTextChange: (String) => _)

  lazy val reactClass = React.createClass[WrappedProps, Unit](
    render = (context) =>
      <.div()(
        <.input(
          ^.value := context.props.wrapped.text,
          ^.onChange := ((event: InputFormSyntheticEvent) => {
            context.props.wrapped.onTextChange(event.target.value)
          })
        )()
      )
  )
}
