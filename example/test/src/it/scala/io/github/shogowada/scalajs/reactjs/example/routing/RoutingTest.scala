package io.github.shogowada.scalajs.reactjs.example.routing

import io.github.shogowada.scalajs.reactjs.example.{BaseTest, TestTargetServers}
import org.openqa.selenium.Alert

class RoutingTest extends BaseTest {

  val server = TestTargetServers.routing

  "given I am at home page" - {
    go to server.host

    "then it should not display about" in {
      find("about") should equal(None)
    }

    "then it should not display repos" in {
      find("repos") should equal(None)
    }

    "when I click on about link" - {
      clickOn(linkText("About"))

      itShouldDisplayAbout()

      "when I jump to repos via URL" - {
        goToRepos()

        itShouldDisplayRepos()

        "when I push /about via history API" - {
          clickOn(id("push-about"))

          itShouldDisplayAbout()

          "when I go back via history API" - {
            clickOn(id("go-back"))

            itShouldDisplayRepos()

            "when I go forward via history API" - {
              clickOn(id("go-forward"))

              itShouldDisplayAbout()
            }
          }
        }
      }
    }

    "when I click on repos link" - {
      clickOn(linkText("Repos"))

      itShouldDisplayRepos()

      "when I jump to specific repo" - {
        val repoId = 123
        goToRepo(repoId)

        "then it should display the repo" in {
          find(s"repo-$repoId").isDefined should equal(true)
        }
      }

      "when I jump to about via URL" - {
        goToAbout()

        itShouldDisplayAbout()
      }
    }

    "when I go to form route" - {
      goToForm()

      itShouldDisplayForm()

      "and it is to confirm before leave" - {
        confirmBeforeLeave()

        "when I try to go to about page" - {
          goToAbout()

          "then it should show confirmation box" in {
            eventually {
              val alert: Alert = webDriver.switchTo().alert()
              alert.getText should equal("Are you sure you want to leave the page?")
              alert.dismiss()
            }
          }

          "when I accept the confirmation" - {
            webDriver.switchTo().alert().accept()

            itShouldDisplayAbout()
          }

          "when I dismiss the confirmation" - {
            webDriver.switchTo().alert().dismiss()

            itShouldDisplayForm()
          }
        }
      }

      "and it is not to confirm before leave" - {
        doNotConfirmBeforeLeave()

        "when I try to go to about page" - {
          goToAbout()

          itShouldDisplayAbout()
        }
      }

      "and I unset route leave hook" - {
        clickOn(id("unset-route-leave-hook"))

        "when I try to got to about page" - {
          goToAbout()

          itShouldDisplayAbout()
        }
      }
    }
  }

  def goToAbout(): Unit = goTo(s"${server.host}/#/about")
  def goToRepos(): Unit = goTo(s"${server.host}/#/repos")
  def goToRepo(id: Int): Unit = goTo(s"${server.host}/#/repos/$id")
  def goToForm(): Unit = goTo(s"${server.host}/#/form")

  def itShouldDisplayAbout(): Unit = itShouldDisplay("about")
  def itShouldDisplayRepos(): Unit = itShouldDisplay("repos")
  def itShouldDisplayForm(): Unit = itShouldDisplay("form")

  def itShouldDisplay(elementId: String): Unit =
    s"then it should display $elementId" in {
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

  quit()
}
