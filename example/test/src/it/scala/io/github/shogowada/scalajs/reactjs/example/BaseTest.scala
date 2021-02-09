package io.github.shogowada.scalajs.reactjs.example

import org.openqa.selenium.chrome.{ChromeDriver, ChromeOptions}
import org.openqa.selenium.remote.{CapabilityType, DesiredCapabilities}
import org.openqa.selenium.{UnexpectedAlertBehaviour, WebDriver}
import org.scalatest.concurrent.Eventually
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatestplus.selenium._

object BaseTest {

  private lazy val webDriver: WebDriver = {
    val capabilities = new DesiredCapabilities()
    capabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE)
    new ChromeDriver(new ChromeOptions().merge(capabilities))
  }

  Runtime.getRuntime.addShutdownHook(new Thread(() => webDriver.quit()))
}

trait BaseTest extends AnyFreeSpec
    with WebBrowser
    with Driver
    with should.Matchers
    with Eventually {

  override implicit val patienceConfig: PatienceConfig = PatienceConfig(
    timeout = scaled(Span(1, Seconds)),
    interval = scaled(Span(50, Millis))
  )

  implicit val webDriver: WebDriver = BaseTest.webDriver
}
