package io.github.shogowada.scalajs.reactjs.example

import java.net.ServerSocket

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.ResourceHandler

class TestTargetServer(project: String) {
  private var server: Server = _

  def start(): Unit = {
    val projectPath = s"target.path.$project"
    val maybeTarget = sys.props.get(projectPath)
    assert(maybeTarget.isDefined, s"Env var: $projectPath is not set")
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

  lazy val customVirtualDOM = init(new TestTargetServer("custom-virtual-dom"))
  lazy val helloWorld = init(new TestTargetServer("helloworld"))
  lazy val helloWorldFunction = init(new TestTargetServer("helloworld-function"))
  lazy val interactiveHelloWorld = init(new TestTargetServer("interactive-helloworld"))
  lazy val lifecycle = init(new TestTargetServer("lifecycle"))
  lazy val reduxDevTools = init(new TestTargetServer("redux-devtools"))
  lazy val reduxMiddleware = init(new TestTargetServer("redux-middleware"))
  lazy val router = init(new TestTargetServer("router"))
  lazy val routerRedux = init(new TestTargetServer("router-redux"))
  lazy val style = init(new TestTargetServer("style"))
  lazy val todoApp = init(new TestTargetServer("todo-app"))
  lazy val todoAppRedux = init(new TestTargetServer("todo-app-redux"))

  def init(server: TestTargetServer): TestTargetServer = {
    server.start()
    sys.addShutdownHook { () =>
      server.stop()
    }
    server
  }
}
