package io.github.shogowada.scalajs.reactjs.example.routing

import java.util.regex.{Matcher, Pattern}

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
    }
  }

  def goToAbout(): Unit = goToRoute("/about")
  def goToRepos(): Unit = goToRoute("/repos")
  def goToRepo(id: Int): Unit = goToRoute(s"/repos/$id")
  def goToForm(): Unit = goToRoute("/form")

  def goToRoute(route: String): Unit = {
    val matcher: Matcher = Pattern.compile("""^([^#]+#).*""").matcher(currentUrl)
    val baseUrl = if (matcher.matches()) {
      matcher.group(1)
    } else {
      server.host + "/#"
    }
    goTo(baseUrl + route)
  }

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
}
