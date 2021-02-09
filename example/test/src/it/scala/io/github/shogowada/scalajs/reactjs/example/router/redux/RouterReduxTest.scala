package io.github.shogowada.scalajs.reactjs.example.router.redux

import io.github.shogowada.scalajs.reactjs.example.{BaseTest, TestTargetServers}

class RouterReduxTest extends BaseTest {

  private lazy val server = TestTargetServers.routerRedux

  private val textA = "text A"
  private val textB = "text B"

  "given" - {

    "I am on the page" in {
      go to server.host
    }

    "when I" - {

      "push route A" in {
        pushRouteA()
      }

      thenIShouldBeOnRouteA()

      "and when I" - {

        s"put text $textA" in {
          textField(tagName("input")).value = textA
        }

        thenItShouldDisplay(textA)
      }
    }

    "then when I" - {

      "push route B" in {
        pushRouteB()
      }

      thenIShouldBeOnRouteB()
      thenItShouldDisplay("")

      "and when I" - {

        s"put text $textB" in {
          textField(tagName("input")).value = textB
        }

        thenItShouldDisplay(textB)
      }
    }

    "then when I again" - {

      "push route A" in {
        pushRouteA()
      }

      thenIShouldBeOnRouteA()
      thenItShouldDisplay(textA)

      "and" - {

        "push route B" in {
          pushRouteB()
        }

        thenIShouldBeOnRouteB()
        thenItShouldDisplay(textB)
      }
    }

    "then when I" - {

      "go -3" in {
        clickOn(id("go-negative-3"))
      }

      thenIShouldBeOnRouteA()
      thenItShouldDisplay(textA)

      "and when I" - {

        "go back" in {
          clickOn(id("go-back"))
        }

        thenIShouldBeOnRouteB()
      }

      "then when I" - {

        "go forward" in {
          clickOn(id("go-forward"))
        }

        thenIShouldBeOnRouteA()
      }
    }
  }

  def pushRouteA(): Unit = clickOn(id("push-route-a"))
  def pushRouteB(): Unit = clickOn(id("push-route-b"))

  def thenItShouldDisplay(text: String): Unit = {
    s"then it should display $text" in {
      eventually {
        textField(tagName("input")).value should equal(text)
      }
    }
  }

  def thenIShouldBeOnRouteA(): Unit = {
    "then I should be on route A" in {
      eventually {
        find(id("path")).get.text should equal("/a")
      }
    }
  }

  def thenIShouldBeOnRouteB(): Unit = {
    "then I should be on route B" in {
      eventually {
        find(id("path")).get.text should equal("/b")
      }
    }
  }
}
