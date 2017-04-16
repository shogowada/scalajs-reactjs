package io.github.shogowada.scalajs.reactjs.example.redux.devtools

import io.github.shogowada.scalajs.reactjs.example.{BaseTest, TestTargetServers}

class ReduxDevToolsTest extends BaseTest {

  val server = TestTargetServers.reduxDevTools

  "given I am on the page" - {
    go to server.host

    "when I type in text" - {
      val text = "Hello, World!"
      textField(tagName("input")).value = text

      "then it should take the text" in {
        eventually {
          textField(tagName("input")).value should equal(text)
        }
      }
    }
  }
}
