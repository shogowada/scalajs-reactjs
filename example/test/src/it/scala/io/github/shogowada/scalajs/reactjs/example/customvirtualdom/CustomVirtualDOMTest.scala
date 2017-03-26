package io.github.shogowada.scalajs.reactjs.example.customvirtualdom

import io.github.shogowada.scalajs.reactjs.example.TestTargetServers
import org.scalatest.concurrent.Eventually
import org.scalatest.selenium.Firefox
import org.scalatest.{Matchers, path}

class CustomVirtualDOMTest extends path.FreeSpec
    with Matchers
    with Eventually
    with Firefox {

  val server = TestTargetServers.customVirtualDOM

  "given I am in the page" - {
    go to server.host

    "then it should display Hello, World!" in eventually {
      find(id("hello-world")).map(_.text) should equal(Some("Hello, World!"))
    }
  }

  close()
}
