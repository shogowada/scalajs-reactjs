package io.github.shogowada.scalajs.reactjs.redux

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.classes.specs.ReactClassSpec
import io.github.shogowada.scalajs.reactjs.elements.ReactElement

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSImport}

trait Action {
  @JSExport("type")
  val `type`: String
}

class ContainerComponent[OwnProps](wrappedClass: ReactClass) {
  def apply(): ReactElement = {
    React.createElement(wrappedClass, new js.Object())
  }

  def apply(ownProps: OwnProps): ReactElement = {
    React.createElement(wrappedClass, ownProps)
  }

  def apply(ownProps: OwnProps, children: js.Any*): ReactElement = {
    React.createElement(wrappedClass, ownProps, children: _*)
  }
}

object Redux {
  type Dispatch = Action => Unit

  def connectAdvanced[State, OwnProps, Props](
      selectorFactory: Dispatch => (State, OwnProps) => Props
  )(
      classSpec: ReactClassSpec
  ): ContainerComponent[OwnProps] = {
    val nativeContainerComponent: ReactClass = NativeRedux.connectAdvanced(
      (dispatch: Dispatch) => {
        val selector = selectorFactory(dispatch)
        (state: State, ownProps: OwnProps) => {
          val props: Props = selector(state, ownProps)
          classSpec.propsToRawJs(props.asInstanceOf[classSpec.Props])
        }
      }
    )(React.createClass(classSpec))

    new ContainerComponent[OwnProps](nativeContainerComponent)
  }
}

@js.native
@JSImport("react-redux", JSImport.Namespace)
object NativeRedux extends js.Object {
  def connectAdvanced[State, OwnProps](selectorFactory: js.Function1[Redux.Dispatch, js.Function2[State, OwnProps, js.Any]]): js.Function1[ReactClass, ReactClass] = js.native
}
