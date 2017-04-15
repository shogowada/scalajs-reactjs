package io.github.shogowada.scalajs.reactjs.example.style

import io.github.shogowada.scalajs.reactjs.example.{BaseTest, TestTargetServers}

class StyleTest extends BaseTest {

  val server = TestTargetServers.style

  "given I am on the page" - {
    go to server.host

    "then it should show green text" in {
      find(id("green-text")).flatMap(_.attribute("class")) should equal(Option("green"))
    }

    "then it should show big blue text" in {
      find(id("big-blue-text")).flatMap(_.attribute("class")) should equal(Option("big blue"))
    }

    "then it should show red text" in {
      find(id("red-text")).flatMap(_.attribute("style")) should equal(Option("color: red;"))
    }
  }
}
