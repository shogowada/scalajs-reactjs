package io.github.shogowada.scalajs.reactjs.example.interactive.helloworld

import io.github.shogowada.scalajs.reactjs.example.TestTargetServers
import org.scalatest.concurrent.Eventually
import org.scalatest.selenium.Firefox
import org.scalatest.{Matchers, path}

class MainTest extends path.FunSpec
    with Matchers
    with Eventually
    with Firefox {

  val server = TestTargetServers.interactiveHelloWorld

  val defaultName = "whoever you are"

  describe("given I am in the homepage") {
    go to server.host

    it("then it should display the default name in input") {
      eventually {
        textField("name").value should be(defaultName)
      }
    }

    it("then it should greed the default name") {
      eventually {
        find("greed").get.text should be(s"Hello, $defaultName!")
      }
    }
  }

  close()
}
