package io.github.shogowada.scalajs.reactjs.example

import org.scalatest.concurrent.Eventually
import org.scalatest.selenium.Chrome
import org.scalatest.{Matchers, path}

trait BaseTest extends path.FreeSpec
    with Chrome
    with Matchers
    with Eventually
