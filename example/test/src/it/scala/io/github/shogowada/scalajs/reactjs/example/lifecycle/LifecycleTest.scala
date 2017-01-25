package io.github.shogowada.scalajs.reactjs.example.lifecycle

import io.github.shogowada.scalajs.reactjs.example.TestTargetServers
import org.scalatest.concurrent.Eventually
import org.scalatest.selenium.Firefox
import org.scalatest.{Matchers, path}

class LifecycleTest extends path.FreeSpec
    with Firefox
    with Eventually
    with Matchers {

  val target = TestTargetServers.lifecycle

  "given I loaded lifecycle page" - {
    go to target.host

    "it should have mounted the component" - {
      eventually {
        checkbox("component-did-mount").isSelected should be(true)
      }
    }

    "it should have updated the component" - {
      eventually {
        checkbox("component-did-update").isSelected should be(true)
      }
    }
  }

  close()
}
