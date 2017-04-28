package io.github.shogowada.scalajs.reactjs.example.redux.middleware

import io.github.shogowada.scalajs.reactjs.example.{BaseTest, TestTargetServers}

class ReduxMiddlewareTest extends BaseTest {
  val server = TestTargetServers.reduxMiddleware

  "given I am on the page" - {
    go to server.host

    "when I add 3" - {
      setValue(3)
      clickOn(id("add"))

      "then it should actually add 3 * 2 = 6 because of the middleware" in {
        eventually {
          getValue should equal(6)
        }
      }

      "when I subtract 2" - {
        setValue(2)
        clickOn(id("subtract"))

        "then it should take a snapshot before the subtraction because of the middleware" in {
          eventually {
            getSnapshot should equal(Option(6))
          }
        }

        "then it should subtract 2" in {
          eventually {
            getValue should equal(4)
          }
        }
      }

      "when I add 6 using future" - {
        setValue(6)
        clickOn(id("add-future"))

        "then it should add 12 because of the middleware" in {
          eventually {
            getValue should equal(18)
          }
        }
      }

      "when I try to add 6 using future but it failed" - {
        setValue(6)
        clickOn(id("add-future-failure"))

        "then it should not add the value" in {
          eventually {
            getValue should equal(6)
          }
        }

        "then it should display the error message" in {
          eventually {
            getError should equal(Option("This is a test. Do not panic."))
          }
        }
      }
    }
  }

  def getValue: Int = find(id("value")).get.text.toInt
  def setValue(value: Int) = textField(id("input")).value = value.toString

  def getSnapshot: Option[Int] = find(id("snapshot")).map(_.text.toInt)

  def getError: Option[String] = find(id("error")).map(_.text)
}
