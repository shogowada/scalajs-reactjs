package io.github.shogowada.scalajs.reactjs.example

import org.openqa.selenium.UnexpectedAlertBehaviour
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.remote.{CapabilityType, DesiredCapabilities}
import org.scalatest.concurrent.Eventually
import org.scalatest.selenium.{Driver, WebBrowser}
import org.scalatest.{Matchers, path}

object BaseTest {
  val webDriver = {
    val capabilities = new DesiredCapabilities()
    capabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE)
    new ChromeDriver()
  }

  Runtime.getRuntime.addShutdownHook(new Thread(() => webDriver.quit()))
}

trait BaseTest extends path.FreeSpec
    with WebBrowser with Driver
    with Matchers
    with Eventually {
  override implicit val webDriver = BaseTest.webDriver
}
