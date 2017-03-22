package io.github.shogowada.scalajs.reactjs.example.todoappredux

import io.github.shogowada.scalajs.reactjs.example.TestTargetServers
import org.scalatest.concurrent.Eventually
import org.scalatest.selenium.Firefox
import org.scalatest.{Matchers, path}

class TodoAppReduxTest extends path.FreeSpec
    with Matchers
    with Eventually
    with Firefox {

  val server = TestTargetServers.todoAppRedux

  "when I am at the page" - {
    go to server.host

    "and I add a todo item" - {
      val firstTodoItem = "First thing to do"
      addTodoItem(firstTodoItem)

      "then it should add the item" in verifyTodoItems(Seq(firstTodoItem))

      "and I add another one" - {
        val secondTodoItem = "Second thing to do"
        addTodoItem(secondTodoItem)

        "then it should add the item" in verifyTodoItems(Seq(
          firstTodoItem,
          secondTodoItem
        ))

        "and I completed the second todo item" - {
          completeTodoItem(secondTodoItem)

          "then it should complete the second todo" in eventually {
            findTodoItem(secondTodoItem)
                .map(_.underlying.getCssValue("text-decoration")) should equal(Some("line-through"))
          }

          "but it should not complete the first todo" in eventually {
            findTodoItem(firstTodoItem)
                .map(_.underlying.getCssValue("text-decoration")) should equal(Some("none"))
          }


          "when I display only active items" - {
            clickOn(findAll(tagName("a")).find(_.text == "Active").get)

            "then it should only display active todo" in verifyTodoItems(Seq(firstTodoItem))
          }

          "when I display only completed items" - {
            clickOn(findAll(tagName("a")).find(_.text == "Completed").get)

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

  def verifyTodoItems(texts: Seq[String]): Unit = eventually {
    findAll(tagName("li")).map(_.text).toSeq should equal(texts)
  }

  close()
}
