package io.github.shogowada.scalajs.reactjs.example.style

import io.github.shogowada.scalajs.reactjs.example.TestTargetServers
import org.scalatest.concurrent.Eventually
import org.scalatest.selenium.Firefox
import org.scalatest.{Matchers, path}

class StyleTest extends path.FreeSpec
    with Matchers
    with Eventually
    with Firefox {

  val server = TestTargetServers.style

  "given I am on the page" - {
    go to server.host

    "then it should show green text" in {
      find(id("green-text")).flatMap(_.attribute("class")) should equal(Option("green"))
    }

    "then it should show blue big text" in {
      find(id("blue-big-text")).flatMap(_.attribute("class")) should equal(Option("blue big"))
    }

    "then it should show red text" in {
      find(id("red-text")).flatMap(_.attribute("style")) should equal(Option("color: red;"))
    }
  }

  close()
}
