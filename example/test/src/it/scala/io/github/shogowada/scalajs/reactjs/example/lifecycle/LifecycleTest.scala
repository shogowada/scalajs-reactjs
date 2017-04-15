package io.github.shogowada.scalajs.reactjs.example.lifecycle

import io.github.shogowada.scalajs.reactjs.example.{BaseTest, TestTargetServers}

class LifecycleTest extends BaseTest {

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
}
