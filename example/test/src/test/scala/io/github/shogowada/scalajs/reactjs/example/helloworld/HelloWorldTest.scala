package io.github.shogowada.scalajs.reactjs.example.helloworld

import io.github.shogowada.scalajs.reactjs.example.TestTargetServers
import org.scalatest.concurrent.Eventually
import org.scalatest.selenium.Firefox
import org.scalatest.{Matchers, path}

class HelloWorldTest extends path.FunSpec
    with Matchers
    with Eventually
    with Firefox {

  val server = TestTargetServers.helloWorld

  describe("given I am in the homepage") {
    go to server.host

    it("then it should display Hello World") {
      eventually {
        find("hello-world").get.text should be("Hello, World!")
      }
    }
  }

  close()
}
