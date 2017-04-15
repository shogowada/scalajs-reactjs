package io.github.shogowada.scalajs.reactjs.example.helloworld

import io.github.shogowada.scalajs.reactjs.example.{BaseTest, TestTargetServers}

class HelloWorldTest extends BaseTest {

  val server = TestTargetServers.helloWorld

  "given I am in the homepage" - {
    go to server.host

    "then it should display Hello World" in {
      eventually {
        find("hello-world").get.text should be("Hello, World!")
      }
    }
  }
}
