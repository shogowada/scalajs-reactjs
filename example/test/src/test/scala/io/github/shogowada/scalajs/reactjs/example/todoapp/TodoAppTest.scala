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

    it("then it should display #1 on the button") {
      eventually {
        find(tagName("button")).get.text should equal("Add #1")
      }
    }

    describe("when I add a TODO item") {
      val newTodoItem = "new TODO item"
      addTodoItem(newTodoItem)

      it("then it should add the item to the list") {
        eventually {
          find(tagName("li")).get.text should equal(newTodoItem)
        }
      }

      it("then it should display #2 on the button") {
        eventually {
          find(tagName("button")).get.text should equal("Add #2")
        }
      }

      it("then it should clear the text") {
        eventually {
          textField(tagName("input")).value should equal("")
        }
      }

      describe("when I add another TODO item") {
        val anotherTodoItem = "another TODO item"
        addTodoItem(anotherTodoItem)

        it("then it should add the item to the list") {
          eventually {
            findAll(tagName("li")).map(_.text).toSeq should equal(Seq(
              newTodoItem,
              anotherTodoItem
            ))
          }
        }
      }
    }
  }

  def addTodoItem(todoItem: String): Unit = {
    textField(tagName("input")).value = todoItem
    submit()
  }

  close()
}
