package io.github.shogowada.scalajs.reactjs.example.interactive.helloworld

import io.github.shogowada.scalajs.reactjs.example.TestTargetServers
import org.scalatest.concurrent.Eventually
import org.scalatest.selenium.Firefox
import org.scalatest.{Matchers, path}

class InteractiveHelloWorldTest extends path.FunSpec
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

    it("then it should greet the default name") {
      eventually {
        find("greet").get.text should be(s"Hello, $defaultName!")
      }
    }

    describe("when I changed the name") {
      val newName = "React JS"

      textField("name").value = newName

      it("then it should greet the new name") {
        eventually {
          find("greet").get.text should be(s"Hello, $newName!")
        }
      }
    }
  }

  close()
}
