package io.github.shogowada.scalajs.reactjs.example.helloworld

import io.github.shogowada.scalajs.reactjs.example.TestTargetServers
import org.scalatest.concurrent.Eventually
import org.scalatest.path
import org.scalatest.selenium.Firefox

class MainTest extends path.FunSpec
    with Eventually
    with Firefox {

  val server = TestTargetServers.helloWorld

  describe("given I am in the homepage") {
    go to server.host

    it("then it should display Hello World") {
      eventually {
        assert(find("hello-world").map(_.text).contains("Hello, World!"))
      }
    }
  }

  close()
}
