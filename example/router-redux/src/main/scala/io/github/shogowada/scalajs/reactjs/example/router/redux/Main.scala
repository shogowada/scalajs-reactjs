package io.github.shogowada.scalajs.reactjs.example.router.redux

import io.github.shogowada.scalajs.history.History
import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.events.FormSyntheticEvent
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

/*
* Extend RouterProps to access router props (e.g. props.location, props.history, and props.match)
* */
object RouteControllerComponent extends RouterProps {

  lazy val reactClass = ReactRedux.connectAdvanced(
    (dispatch: Dispatch) => {
      val act = (action: NativeAction) => dispatch(action)
      val pushRouteA = () => dispatch(Push("/a"))
      val pushRouteB = () => dispatch(Push("/b"))
      val goNegative3 = () => dispatch(Go(-1))
      val goBack = () => dispatch(GoBack())
      val goForward = () => dispatch(GoForward())
      (state: State, ownProps: Props[Unit]) => {
        RouteControllerPresentationalComponent.WrappedProps(
          path = ownProps.location.pathname,
          onPushRouteAClick = pushRouteA,
          onPushRouteBClick = pushRouteB,
          onGoNegative3Click = goNegative3,
          onGoBackClick = goBack,
          onGoForwardClick = goForward
        )
      }
    }
  )(RouteControllerPresentationalComponent.reactClass)
}

object RouteControllerPresentationalComponent {
  case class WrappedProps(
      path: String,
      onPushRouteAClick: () => _,
      onPushRouteBClick: () => _,
      onGoNegative3Click: () => _,
      onGoBackClick: () => _,
      onGoForwardClick: () => _
  )

  type Self = React.Self[WrappedProps, Unit]

  lazy val reactClass = React.createClass[WrappedProps, Unit](
    (self) =>
      <.div()(
        <.h3()("Path: ", <.span(^.id := "path")(self.props.wrapped.path)),
        <.div()(
          <.button(^.id := "push-route-a", ^.onClick := self.props.wrapped.onPushRouteAClick)("Push route A"),
          <.button(^.id := "push-route-b", ^.onClick := self.props.wrapped.onPushRouteBClick)("Push route B"),
          <.button(^.id := "go-negative-3", ^.onClick := self.props.wrapped.onGoNegative3Click)("Go -3"),
          <.button(^.id := "go-back", ^.onClick := self.props.wrapped.onGoBackClick)("Go back"),
          <.button(^.id := "go-forward", ^.onClick := self.props.wrapped.onGoForwardClick)("Go forward")
        ),
        <.Switch()(
          <.Route(^.path := "/a", ^.component := ARouteComponent.reactClass)(),
          <.Route(^.path := "/b", ^.component := BRouteComponent.reactClass)()
        )
      )
  )
}

object ARouteComponent {
  lazy val reactClass = ReactRedux.connectAdvanced(
    (dispatch: Dispatch) => {
      val onTextChange = (text: String) => dispatch(ChangeTextA(text))
      (state: State, ownProps: Props[Unit]) => {
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
      (state: State, ownProps: Props[Unit]) => {
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
