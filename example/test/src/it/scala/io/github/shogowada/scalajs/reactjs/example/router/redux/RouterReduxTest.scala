package io.github.shogowada.scalajs.reactjs.example.router.redux

import io.github.shogowada.scalajs.reactjs.example.{BaseTest, TestTargetServers}

class RouterReduxTest extends BaseTest {
  val server = TestTargetServers.routerRedux

  "given I am on the page" - {
    go to server.host

    "when I push route A" - {
      pushRouteA()

      thenIShouldBeOnRouteA()

      "when I push route B" - {
        pushRouteB()

        thenIShouldBeOnRouteB()

        "when I push route A again" - {
          pushRouteA()

          thenIShouldBeOnRouteA()

          "when I push route B again" - {
            pushRouteB()

            thenIShouldBeOnRouteB()

            "when I go -3" - {
              clickOn(id("go-negative-3"))

              thenIShouldBeOnRouteA()
            }
          }
        }

        "when I go back" - {
          clickOn(id("go-back"))

          thenIShouldBeOnRouteA()

          "when I go forward" - {
            clickOn(id("go-forward"))

            thenIShouldBeOnRouteB()
          }
        }
      }
    }
  }

  def pushRouteA(): Unit = clickOn(id("push-route-a"))
  def pushRouteB(): Unit = clickOn(id("push-route-b"))

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
