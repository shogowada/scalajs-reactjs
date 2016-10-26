package io.github.shogowada.scalajs.reactjs.example

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.ResourceHandler

class TestTargetServer(project: String) {
  private var server: Server = _

  def start(): Unit = {
    val maybeTarget = sys.props.get(s"target.path.$project")
    assert(maybeTarget.isDefined)
    val target = maybeTarget.get
    println(s"Target for $project: $target")

    server = new Server(8080)
    val handler = new ResourceHandler()
    handler.setResourceBase(s"$target")
    server.setHandler(handler)
    server.start()
  }

  def host: String = "http://localhost:8080/classes"

  def stop(): Unit = {
    server.stop()
  }
}

object TestTargetServers {
  val helloWorld = new TestTargetServer("helloworld")

  helloWorld.start()

  sys.addShutdownHook(() => {
    helloWorld.stop()
  })
}
