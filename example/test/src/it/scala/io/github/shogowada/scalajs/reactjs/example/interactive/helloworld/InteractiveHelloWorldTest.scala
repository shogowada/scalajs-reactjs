package io.github.shogowada.scalajs.reactjs.example.interactive.helloworld

import io.github.shogowada.scalajs.reactjs.example.{BaseTest, TestTargetServers}

class InteractiveHelloWorldTest extends BaseTest {

  private lazy val server = TestTargetServers.interactiveHelloWorld

  val defaultName = "whoever you are"
  val newName = "React JS"

  "given" - {

    "I am in the homepage" in {
      go to server.host
    }

    "then it should display the default name in input" in {
      eventually {
        textField("name").value should be(defaultName)
      }
    }

    "then it should greet the default name" in {
      eventually {
        find("greet").get.text should be(s"Hello, $defaultName!")
      }
    }

    "when I" - {

      "changed the name" in {
        textField("name").value = newName
      }

      "then it should greet the new name" in {
        eventually {
          find("greet").get.text should be(s"Hello, $newName!")
        }
      }
    }

    "when I" - {

      "checked the lower case radio box" in {
        radioButtonGroup("letter-case").value = "Lower Case"

      }

      "then it should display the name in lower case" in {
        eventually {
          find("greet").get.text should be(s"Hello, ${newName.toLowerCase}!")
        }
      }
    }

    "when I" - {

      "checked the upper case radio box" in {
        radioButtonGroup("letter-case").value = "Upper Case"

      }

      "then it should display the name in upper case" in {
        eventually {
          find("greet").get.text should be(s"Hello, ${newName.toUpperCase}!")
        }
      }
    }
  }
}
