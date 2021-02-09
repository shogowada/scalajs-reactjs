package io.github.shogowada.scalajs.reactjs.example.todoappredux

import io.github.shogowada.scalajs.reactjs.example.{BaseTest, TestTargetServers}

class TodoAppReduxTest extends BaseTest {

  private lazy val server = TestTargetServers.todoAppRedux

  "given" - {

    "I am at home page" in {
      go to server.host
    }

    "and" - {
      val firstTodoItem = "First thing to do"

      "I add a todo item" in {
        addTodoItem(firstTodoItem)
      }

      "then it should add the item" in {
        verifyTodoItems(Seq(firstTodoItem))
      }

      "and" - {
        val secondTodoItem = "Second thing to do"

        "I add another one" in {
          addTodoItem(secondTodoItem)
        }

        "then it should add the item" in {
          verifyTodoItems(Seq(firstTodoItem, secondTodoItem))
        }

        "and" - {
          "I completed the second todo item" in {
            completeTodoItem(secondTodoItem)
          }

          "then it should complete the second todo" in eventually {
            findTodoItemOrFail(secondTodoItem)
                .underlying.getCssValue("text-decoration") should startWith("line-through")
          }

          "but it should not complete the first todo" in eventually {
            findTodoItemOrFail(firstTodoItem)
                .underlying.getCssValue("text-decoration") should startWith("none")
          }

          "when I" - {
            "display only active items" in {
              clickOn(findAll(tagName("a")).find(_.text == "Active").get)
            }

            "then it should only display active todo" in verifyTodoItems(Seq(firstTodoItem))
          }

          "when I" - {
            "display only completed items" in {
              clickOn(findAll(tagName("a")).find(_.text == "Completed").get)
            }

            "then it should only display completed todo" in verifyTodoItems(Seq(secondTodoItem))
          }
        }
      }
    }
  }

  def addTodoItem(text: String): Unit = {
    textField(tagName("input")).value = text
    submit()
  }

  def completeTodoItem(text: String): Unit =
    clickOn(findTodoItem(text).get)


  def findTodoItem(text: String): Option[Element] =
    findAll(tagName("li")).find(_.text == text)

  def findTodoItemOrFail(text: String): Element =
    findTodoItem(text).getOrElse {
      throw new AssertionError(s"Expected todo item '$text' to be present")
    }

  def verifyTodoItems(texts: Seq[String]): Unit = eventually {
    findAll(tagName("li")).map(_.text).toSeq should equal(texts)
  }
}
