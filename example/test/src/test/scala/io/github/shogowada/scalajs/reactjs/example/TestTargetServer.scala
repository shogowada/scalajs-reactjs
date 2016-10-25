package io.github.shogowada.scalajs.reactjs.example

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.ResourceHandler

class TestTargetServer(project: String) {
  private var server: Server = _

  def start(): Unit = {
    server = new Server(8080)
    val handler = new ResourceHandler()
    handler.setResourceBase(s"./example/$project/target/scala-2.11")
    server.setHandler(handler)
    server.start()
  }

  def host: String = "http://localhost:8080/classes"

  def stop(): Unit = {
    server.stop()
  }
}
