package io.github.shogowada.scalajs.reactjs.example.todoapp

import io.github.shogowada.scalajs.reactjs.example.TestTargetServers
import org.scalatest.concurrent.Eventually
import org.scalatest.selenium.Firefox
import org.scalatest.{Matchers, path}

class TodoAppTest extends path.FunSpec
    with Matchers
    with Eventually
    with Firefox {

  val server = TestTargetServers.todoApp

  describe("given I am at the page") {
    go to server.host

    it("then it should display the header") {
      eventually {
        find(tagName("h3")).get.text should equal("TODO")
      }
    }

    it("then it should have no TODO items") {
      eventually {
        find(tagName("li")) should equal(None)
      }
    }
  }

  close()
}
