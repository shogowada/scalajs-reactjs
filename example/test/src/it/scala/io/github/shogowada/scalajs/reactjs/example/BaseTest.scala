package io.github.shogowada.scalajs.reactjs.example

import org.openqa.selenium.chrome.ChromeDriver
import org.scalatest.concurrent.Eventually
import org.scalatest.selenium.{Driver, WebBrowser}
import org.scalatest.{Matchers, path}

object BaseTest {
  val webDriver = new ChromeDriver()

  Runtime.getRuntime.addShutdownHook(new Thread(() => webDriver.quit()))
}

trait BaseTest extends path.FreeSpec
    with WebBrowser with Driver
    with Matchers
    with Eventually {
  override implicit val webDriver = BaseTest.webDriver
}
