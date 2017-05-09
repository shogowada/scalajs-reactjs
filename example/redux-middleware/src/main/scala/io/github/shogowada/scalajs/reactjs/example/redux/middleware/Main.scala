package io.github.shogowada.scalajs.reactjs.example.redux.middleware

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.events.FormSyntheticEvent
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux._
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.redux.devtools.ReduxDevTools
import io.github.shogowada.scalajs.reactjs.redux.{Action, ReactRedux, Redux, Store}
import io.github.shogowada.scalajs.reactjs.{React, ReactDOM}
import org.scalajs.dom
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js.JSApp
import scala.util.{Failure, Success}

/*
* This example shows how to use Redux middleware facade.
* If you are not yet familiar with middlewares, take a look at http://redux.js.org/docs/advanced/Middleware.html.
* */

case class State(
    result: Int,
    snapshot: Option[Int],
    error: Option[Throwable]
)

case class Add(value: Int) extends Action
case class Subtract(value: Int) extends Action
case class Snapshot(snapshot: Int) extends Action
case class Error(throwable: Throwable) extends Action

object Reducer {
  def reduce(maybeState: Option[State], action: Any): State =
    State(
      result = reduceResult(maybeState.map(_.result), action),
      snapshot = reduceSnapshot(maybeState.flatMap(_.snapshot), action),
      error = reduceError(maybeState.flatMap(_.error), action)
    )

  def reduceResult(maybeResult: Option[Int], action: Any): Int = {
    val result = maybeResult.getOrElse(0)
    action match {
      case action: Add => result + action.value
      case action: Subtract => result - action.value
      case _ => result
    }
  }

  def reduceSnapshot(maybeSnapshot: Option[Int], action: Any): Option[Int] = {
    action match {
      case action: Snapshot => Option(action.snapshot)
      case _ => maybeSnapshot
    }
  }

  def reduceError(maybeError: Option[Throwable], action: Any): Option[Throwable] = {
    action match {
      case action: Error => Option(action.throwable)
      case _ => maybeError
    }
  }
}

object Middleware {
  /*
  * This middleware doubles all the additions.
  * */
  val doubleAddition = (store: Store[State]) => (next: Dispatch) => (action: Any) => {
    action match {
      case action: Add =>
        next(action)
        next(action)
      case _ => next(action)
    }
  }

  /*
  * This middleware takes a snapshot before every subtraction.
  * It shows how to get current state from the store.
  * */
  val snapshotBeforeSubtraction = (store: Store[State]) => (next: Dispatch) => (action: Any) => {
    action match {
      case action: Subtract =>
        next(Snapshot(store.getState.result))
        next(action)
      case _ => next(action)
    }
  }

  /*
  * This middleware resolves futures and call the next dispatcher with the result.
  * If it failed, it dispatches Error action.
  * */
  val futureAction = (store: Store[State]) => (next: Dispatch) => (action: Any) => {
    action match {
      case futureAction: Future[Any] =>
        futureAction.onComplete {
          case Success(action: Any) => store.dispatch(action)
          case Failure(throwable) => store.dispatch(Error(throwable))
        }
      case _ => next(action)
    }
  }
}

object Main extends JSApp {
  def main(): Unit = {
    /*
    * Use Redux.applyMiddleware to create an enhancer for middlewares.
    * You can also use them with ReduxDevTools.composeWithDevTools to enable React DevTools.
    * */
    val store = Redux.createStore(
      Reducer.reduce,
      ReduxDevTools.composeWithDevTools(
        Redux.applyMiddleware(
          Middleware.doubleAddition,
          Middleware.snapshotBeforeSubtraction,
          Middleware.futureAction
        )
      )
    )

    val mountNode = dom.document.getElementById("mount-node")
    ReactDOM.render(
      <.Provider(^.store := store)(
        <(Example.reactClass).empty
      ),
      mountNode
    )
  }
}

object Example {
  lazy val reactClass = ReactRedux.connectAdvanced(
    (dispatch: Dispatch) => {
      val onAdd = (value: Int) => dispatch(Add(value))
      val onAddFuture = (value: Int) => dispatch(Future(Add(value)))
      val onAddFutureFailure = (value: Int) => dispatch(Future.failed(new Exception("This is a test. Do not panic.")))
      val onSubtract = (value: Int) => dispatch(Subtract(value))
      (state: State, ownProps: Props[Unit]) => {
        ExamplePresentational.WrappedProps(
          result = state.result,
          snapshot = state.snapshot,
          error = state.error,
          onAdd = onAdd,
          onAddFuture = onAddFuture,
          onAddFutureFailure = onAddFutureFailure,
          onSubtract = onSubtract
        )
      }
    }
  )(ExamplePresentational.reactClass)
}

object ExamplePresentational {

  case class WrappedProps(
      result: Int,
      snapshot: Option[Int],
      error: Option[Throwable],
      onAdd: (Int) => _,
      onAddFuture: (Int) => _,
      onAddFutureFailure: (Int) => _,
      onSubtract: (Int) => _
  )

  case class State(value: Int)

  type Self = React.Self[WrappedProps, State]

  lazy val reactClass = React.createClass[WrappedProps, State](
    getInitialState = (self) => State(0),
    render = (self) =>
      <.div()(
        self.props.wrapped.error.map(error =>
          <.div()("Error: ", <.span(^.id := "error")(error.getMessage))
        ),
        <.div()("Value: ", <.span(^.id := "value")(self.props.wrapped.result)),
        self.props.wrapped.snapshot.map(snapshot =>
          <.div()("Snapshot: ", <.span(^.id := "snapshot")(snapshot))
        ),
        <.form()(
          <.input(
            ^.id := "input",
            ^.`type`.text,
            ^.value := self.state.value,
            ^.onChange := ((event: FormSyntheticEvent[HTMLInputElement]) => {
              self.setState(State(event.target.value.toInt))
            })
          )(),
          <.input(
            ^.id := "add",
            ^.`type`.button,
            ^.onClick := onAddClick(self),
            ^.value := "Add"
          )(),
          <.input(
            ^.id := "add-future",
            ^.`type`.button,
            ^.onClick := onAddFutureClick(self),
            ^.value := "Add Future"
          )(),
          <.input(
            ^.id := "add-future-failure",
            ^.`type`.button,
            ^.onClick := onAddFutureFailureClick(self),
            ^.value := "Add Future Failure"
          )(),
          <.input(
            ^.id := "subtract",
            ^.`type`.button,
            ^.onClick := onSubtractClick(self),
            ^.value := "Subtract"
          )()
        )
      )
  )

  private def onAddClick(self: Self) =
    () => {
      self.props.wrapped.onAdd(self.state.value)
    }

  private def onAddFutureClick(self: Self) =
    () => {
      self.props.wrapped.onAddFuture(self.state.value)
    }

  private def onAddFutureFailureClick(self: Self) =
    () => {
      self.props.wrapped.onAddFutureFailure(self.state.value)
    }

  private def onSubtractClick(self: Self) =
    () => {
      self.props.wrapped.onSubtract(self.state.value)
    }
}
