package io.github.shogowada.scalajs.reactjs.router

import io.github.shogowada.scalajs.history.History
import io.github.shogowada.scalajs.reactjs.React.Props

trait RouterProps {
  implicit class RouterProps(props: Props[_]) {
    def history: History = props.native.history.asInstanceOf[History]
    def location: Location = props.native.location.asInstanceOf[Location]
    def `match`: Match = props.native.`match`.asInstanceOf[Match]
  }
}

object RouterProps extends RouterProps
