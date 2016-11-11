package io.github.shogowada.scalajs.reactjs.example.routing

import io.github.shogowada.scalajs.reactjs.example.TestTargetServers
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
      }
    }

    describe("when I click on repos link") {
      clickOn(linkText("Repos"))

      itShouldDisplayRepos()

      describe("when I jump to about via URL") {
        go to s"${server.host}/#/about"

        itShouldDisplayAbout()
      }
    }
  }

  def itShouldDisplayAbout(): Unit = {
    it("then it should display about") {
      eventually {
        find("about").isDefined should equal(true)
      }
    }
  }

  def itShouldDisplayRepos(): Unit = {
    it("then it should display repos") {
      eventually {
        find("repos").isDefined should equal(true)
      }
    }
  }

  close()
}
