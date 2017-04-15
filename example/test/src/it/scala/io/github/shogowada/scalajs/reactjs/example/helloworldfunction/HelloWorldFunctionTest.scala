package io.github.shogowada.scalajs.reactjs.example.helloworldfunction

import io.github.shogowada.scalajs.reactjs.example.{BaseTest, TestTargetServers}

class HelloWorldFunctionTest extends BaseTest {

  val server = TestTargetServers.helloWorldFunction

  "given I am in the homepage" - {
    go to server.host

    "then it should display Hello World" in {
      eventually {
        find("hello-world").get.text should be("Hello, World!")
      }
    }
  }

  quit()
}
