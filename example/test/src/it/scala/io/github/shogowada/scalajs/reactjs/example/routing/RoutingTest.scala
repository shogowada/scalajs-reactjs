package io.github.shogowada.scalajs.reactjs.example.routing

import io.github.shogowada.scalajs.reactjs.example.TestTargetServers
import org.openqa.selenium.Alert
import org.scalatest.concurrent.Eventually
import org.scalatest.selenium.Firefox
import org.scalatest.{Matchers, path}

class RoutingTest extends path.FunSpec
    with Matchers
    with Eventually
    with Firefox {

  val server = TestTargetServers.routing

  describe("given I am at home page") {
    go to server.host

    it("then it should not display about") {
      find("about") should equal(None)
    }

    it("then it should not display repos") {
      find("repos") should equal(None)
    }

    describe("when I click on about link") {
      clickOn(linkText("About"))

      itShouldDisplayAbout()

      describe("when I jump to repos via URL") {
        go to s"${server.host}/#/repos"

        itShouldDisplayRepos()

        describe("when I push /about via history API") {
          clickOn(id("push-about"))

          itShouldDisplayAbout()

          describe("when I go back via history API") {
            clickOn(id("go-back"))

            itShouldDisplayRepos()

            describe("when I go forward via history API") {
              clickOn(id("go-forward"))

              itShouldDisplayAbout()
            }
          }
        }
      }
    }

    describe("when I click on repos link") {
      clickOn(linkText("Repos"))

      itShouldDisplayRepos()

      describe("when I jump to specific repo") {
        val repoId = 123
        go to s"${server.host}/#/repos/$repoId"

        it("then it should display the repo") {
          find(s"repo-$repoId").isDefined should equal(true)
        }
      }

      describe("when I jump to about via URL") {
        go to s"${server.host}/#/about"

        itShouldDisplayAbout()
      }
    }

    describe("when I go to form route") {
      go to s"${server.host}/#/form"

      itShouldDisplayForm()

      describe("and it is to confirm before leave") {
        confirmBeforeLeave()

        describe("when I try to go to about page") {
          go to s"${server.host}/#/about"

          it("then it should show confirmation box") {
            eventually {
              val alert: Alert = webDriver.switchTo().alert()
              alert.getText should equal("Are you sure you want to leave the page?")
              alert.dismiss()
            }
          }

          describe("when I accept the confirmation") {
            webDriver.switchTo().alert().accept()

            itShouldDisplayAbout()
          }

          describe("when I dismiss the confirmation") {
            webDriver.switchTo().alert().dismiss()

            itShouldDisplayForm()
          }
        }
      }

      describe("and it is not to confirm before leave") {
        doNotConfirmBeforeLeave()

        describe("when I try to go to about page") {
          go to s"${server.host}/#/about"

          itShouldDisplayAbout()
        }
      }

      describe("and I unset route leave hook") {
        clickOn(id("unset-route-leave-hook"))

        describe("when I try to got to about page") {
          go to s"${server.host}/#/about"

          itShouldDisplayAbout()
        }
      }
    }
  }

  def itShouldDisplayAbout(): Unit = itShouldDisplay("about")
  def itShouldDisplayRepos(): Unit = itShouldDisplay("repos")
  def itShouldDisplayForm(): Unit = itShouldDisplay("form")

  def itShouldDisplay(elementId: String): Unit =
    it(s"then it should display $elementId") {
      eventually {
        find(id(elementId)).isDefined should equal(true)
      }
    }

  def confirmBeforeLeave(): Unit = confirmBeforeLeave(true)
  def doNotConfirmBeforeLeave(): Unit = confirmBeforeLeave(false)

  def confirmBeforeLeave(confirm: Boolean): Unit = {
    val safeToLeaveCheckBox = find(id("confirm-before-leave")).get
    if (safeToLeaveCheckBox.isSelected != confirm) {
      clickOn(safeToLeaveCheckBox)
    }
  }

  close()
}
