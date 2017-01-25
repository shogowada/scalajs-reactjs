package io.github.shogowada.scalajs.reactjs.example

import java.net.ServerSocket

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.ResourceHandler

class TestTargetServer(project: String) {
  private var server: Server = _

  def start(): Unit = {
    val maybeTarget = sys.props.get(s"target.path.$project")
    assert(maybeTarget.isDefined)
    val target = maybeTarget.get
    println(s"Target path for $project: $target")

    val port = freePort
    println(s"Target port for $project: $port")

    server = new Server(port)
    val handler = new ResourceHandler()
    handler.setResourceBase(s"$target")
    server.setHandler(handler)
    server.start()

    println(s"Target host for $project: $host")
  }

  def freePort: Int = {
    var socket: ServerSocket = null
    var port: Int = 0
    try {
      socket = new ServerSocket(0)
      socket.setReuseAddress(true)
      port = socket.getLocalPort
    } finally {
      socket.close()
    }
    port
  }

  def host: String = s"http://localhost:${server.getURI.getPort}/classes"

  def stop(): Unit = {
    server.stop()
  }
}

object TestTargetServers {
  val helloWorld = new TestTargetServer("helloworld")
  val interactiveHelloWorld = new TestTargetServer("interactive-helloworld")
  val routing = new TestTargetServer("routing")
  val todoApp = new TestTargetServer("todo-app")

  helloWorld.start()
  interactiveHelloWorld.start()
  routing.start()
  todoApp.start()

  sys.addShutdownHook(() => {
    helloWorld.stop()
    interactiveHelloWorld.stop()
    routing.stop()
    todoApp.stop()
  })
}
