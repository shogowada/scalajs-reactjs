package io.github.shogowada.scalajs.reactjs.example.todoapp

import io.github.shogowada.scalajs.reactjs.example.{BaseTest, TestTargetServers}

class TodoAppTest extends BaseTest {

  val server = TestTargetServers.todoApp

  "given I am at the page" - {
    go to server.host

    "then it should display the header" in {
      eventually {
        find(tagName("h3")).get.text should equal("TODO")
      }
    }

    "then it should have no TODO items" in {
      eventually {
        find(tagName("li")) should equal(None)
      }
    }

    "then it should display #1 on the button" in {
      eventually {
        find(tagName("button")).get.text should equal("Add #1")
      }
    }

    "when I add a TODO item" - {
      val newTodoItem = "new TODO item"
      addTodoItem(newTodoItem)

      "then it should add the item to the list" in {
        eventually {
          find(tagName("li")).get.text should equal(newTodoItem)
        }
      }

      "then it should display #2 on the button" in {
        eventually {
          find(tagName("button")).get.text should equal("Add #2")
        }
      }

      "then it should clear the text" in {
        eventually {
          textField(tagName("input")).value should equal("")
        }
      }

      "when I add another TODO item" - {
        val anotherTodoItem = "another TODO item"
        addTodoItem(anotherTodoItem)

        "then it should add the item to the list" in {
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
}
