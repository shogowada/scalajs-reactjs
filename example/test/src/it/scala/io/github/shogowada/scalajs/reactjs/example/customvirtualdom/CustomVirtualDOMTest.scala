package io.github.shogowada.scalajs.reactjs.example.customvirtualdom

import io.github.shogowada.scalajs.reactjs.example.{BaseTest, TestTargetServers}

class CustomVirtualDOMTest extends BaseTest {

  val server = TestTargetServers.customVirtualDOM

  "given I am in the page" - {
    go to server.host

    "then it should display Hello, World!" in eventually {
      find(id("hello-world")).map(_.text) should equal(Some("Hello, World!"))
    }
  }
}
