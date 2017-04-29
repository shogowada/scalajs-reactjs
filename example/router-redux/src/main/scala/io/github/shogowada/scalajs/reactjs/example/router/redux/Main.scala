package io.github.shogowada.scalajs.reactjs.example.router.redux

import io.github.shogowada.scalajs.history.History
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.events.{FormSyntheticEvent, SyntheticEvent}
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux._
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.redux.devtools.ReduxDevTools
import io.github.shogowada.scalajs.reactjs.redux.{Action, NativeAction, ReactRedux, Redux}
import io.github.shogowada.scalajs.reactjs.router.Router._
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import io.github.shogowada.scalajs.reactjs.router.redux.ReactRouterRedux
import io.github.shogowada.scalajs.reactjs.router.redux.ReactRouterRedux._
import io.github.shogowada.scalajs.reactjs.router.redux.ReactRouterReduxAction.{Go, GoBack, GoForward, Push}
import io.github.shogowada.scalajs.reactjs.{React, ReactDOM}
import org.scalajs.dom
import org.scalajs.dom.raw.HTMLInputElement

import scala.scalajs.js
import scala.scalajs.js.JSApp

/*
* If you are not yet familiar with react-router-redux,
* see https://github.com/ReactTraining/react-router/tree/master/packages/react-router-redux for details.
* */

/*
* Because we are sharing the Redux state with react-router-redux,
* your state needs to be wrapped.
* */
@js.native
trait State extends js.Object {
  def wrapped: WrappedState = js.native
}

case class WrappedState(textA: String, textB: String)

case class ChangeTextA(text: String) extends Action
case class ChangeTextB(text: String) extends Action

object Reducer {
  val reduce = (maybeState: Option[WrappedState], action: Any) =>
    WrappedState(
      textA = reduceTextA(maybeState.map(_.textA), action),
      textB = reduceTextB(maybeState.map(_.textB), action)
    )

  def reduceTextA(maybeTextA: Option[String], action: Any): String =
    action match {
      case action: ChangeTextA => action.text
      case _ => maybeTextA.getOrElse("")
    }

  def reduceTextB(maybeTextB: Option[String], action: Any): String =
    action match {
      case action: ChangeTextB => action.text
      case _ => maybeTextB.getOrElse("")
    }
}

object Main extends JSApp {
  override def main(): Unit = {
    /*
    * You can use one of the following histories:
    *
    * - History.createBrowserHistory()
    * - History.createHashHistory()
    * - History.createMemoryHistory()
    *
    * See https://www.npmjs.com/package/history for details.
    * */
    val history = History.createHashHistory()

    val store = Redux.createStore(
      /*
      * Combine your reducer with RouterRedux.routerReducer.
      * Note that state of the router needs to be named "router".
      * */
      Redux.combineReducers(Map(
        "wrapped" -> Reducer.reduce,
        "router" -> ReactRouterRedux.routerReducer
      )),
      ReduxDevTools.composeWithDevTools( // DevTools is optional
        Redux.applyMiddleware(
          ReactRouterRedux.routerMiddleware(history)
        )
      )
    )

    /*
    * To access required elements and attributes, import the following:
    *
    * - io.github.shogowada.scalajs.reactjs.VirtualDOM._
    *   - For general elements and attributes
    * - io.github.shogowada.scalajs.reactjs.redux.ReactRedux._
    *   - For Provider and store
    * - io.github.shogowada.scalajs.reactjs.router.Router._
    *   - For history, Switch, Route, and others
    * - io.github.shogowada.scalajs.reactjs.router.redux.RouterRedux._
    *   - For ConnectedRouter
    * */
    val mountNode = dom.document.getElementById("mount-node")
    ReactDOM.render(
      <.Provider(^.store := store)(
        <.ConnectedRouter(^.history := history)(
          <.Route(^.component := RouteControllerComponent.reactClass)()
        )
      ),
      mountNode
    )
  }
}

object RouteControllerComponent {
  lazy val reactClass = ReactRedux.connectAdvanced(
    (dispatch: Dispatch) => {
      val act = (action: NativeAction) => dispatch(action)
      (state: State, ownProps: Unit) => {
        RouteControllerPresentationalComponent.WrappedProps(act)
      }
    }
  )(RouteControllerPresentationalComponent.reactClass)
}

object RouteControllerPresentationalComponent extends RouterProps {
  case class WrappedProps(act: (NativeAction) => _)

  type Self = React.Self[WrappedProps, Unit]

  lazy val reactClass = React.createClass[WrappedProps, Unit](
    (self) =>
      <.div()(
        <.h3()("Path: ", <.span(^.id := "path")(self.props.location.pathname)),
        <.div()(
          <.button(^.id := "push-route-a", ^.onClick := act(self, Push("/a")))("Push route A"),
          <.button(^.id := "push-route-b", ^.onClick := act(self, Push("/b")))("Push route B"),
          <.button(^.id := "go-negative-3", ^.onClick := act(self, Go(-3)))("Go -3"),
          <.button(^.id := "go-back", ^.onClick := act(self, GoBack()))("Go back"),
          <.button(^.id := "go-forward", ^.onClick := act(self, GoForward()))("Go forward")
        ),
        <.Switch()(
          <.Route(^.path := "/a", ^.component := ARouteComponent.reactClass)(),
          <.Route(^.path := "/b", ^.component := BRouteComponent.reactClass)()
        )
      )
  )

  private def act(self: Self, action: NativeAction) =
    (event: SyntheticEvent) => {
      event.preventDefault()
      self.props.wrapped.act(action)
    }
}

object ARouteComponent {
  lazy val reactClass = ReactRedux.connectAdvanced(
    (dispatch: Dispatch) => {
      val onTextChange = (text: String) => dispatch(ChangeTextA(text))
      (state: State, ownProps: Unit) => {
        RoutePresentationalComponent.WrappedProps(
          text = state.wrapped.textA,
          onTextChange = onTextChange
        )
      }
    }
  )(RoutePresentationalComponent.reactClass)
}

object BRouteComponent {
  lazy val reactClass = ReactRedux.connectAdvanced(
    (dispatch: Dispatch) => {
      val onTextChange = (text: String) => dispatch(ChangeTextB(text))
      (state: State, ownProps: Unit) => {
        RoutePresentationalComponent.WrappedProps(
          text = state.wrapped.textB,
          onTextChange = onTextChange
        )
      }
    }
  )(RoutePresentationalComponent.reactClass)
}

object RoutePresentationalComponent {

  case class WrappedProps(text: String, onTextChange: (String) => _)

  lazy val reactClass = React.createClass[WrappedProps, Unit](
    (self) =>
      <.input(
        ^.value := self.props.wrapped.text,
        ^.onChange := ((event: FormSyntheticEvent[HTMLInputElement]) => {
          val text = event.target.value
          self.props.wrapped.onTextChange(text)
        })
      )()
  )
}
