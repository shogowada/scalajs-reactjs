package io.github.shogowada.scalajs.reactjs.example.helloworld

import io.github.shogowada.scalajs.reactjs.example.TestTargetServer
import org.scalatest.concurrent.Eventually
import org.scalatest.path
import org.scalatest.selenium.Chrome

class MainTest extends path.FunSpec
    with Eventually
    with Chrome {

  val server = new TestTargetServer("helloworld")
  server.start()

  describe("given I am in the homepage") {
    go to server.host

    it("then it should display Hello World") {
      eventually {
        assert(find("hello-world").map(_.text).contains("Hello, World!"))
      }
    }
  }

  close()

  server.stop()
}
