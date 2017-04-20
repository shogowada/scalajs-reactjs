# Basics

## How to replace JSX in Scala?

To create elements in Scala, import `VirtualDOM._`. `VirtualDOM` is an extended [Static Tags](https://github.com/shogowada/statictags).

`VirtualDOM` is made of three parts: tag, attributes (a.k.a. props), and children.

For example, this code

```scala
<.div(^.id := "hello-world")("Hello, World!")
```

is equivalent of the following:

```html
<div id="hello-world">Hello, World!</div>
```

You can use as many as attributes and children you want.

## How to create React classes?

Use `React.createClass[WrappedProps, State]` to create React classes (we will explain about `WrappedProps` in next section).

```scala
val reactClass: ReactClass = React.createClass[Unit, Unit]( // If you don't have props or state, use Unit.
  render = (self) => <.div()("Hello, World!")
)
```

To render React classes, use `<(/* ReactClass */)` to make it an element. You can pass attributes and children like regular elements.

```scala
import io.github.shogowada.scalajs.reactjs.VirtualDOM._

val reactClass = React.createClass(/* ... */)

ReactDOM.render(
  <(reactClass)(
    // attributes (a.k.a. props)
    ^.id := "my-component"
  )(
    // children
    <.div()("I'm your component's child!")
  ),
  mountNode
)
```

### What's WrappedProps?

While many want to use case classes as props, React requires props to be a plain JavaScript object. So, to use case classes, we need to wrap them in another property. In this facade, we wrap them in "wrapped" property.

```scala
case class WrappedProps(foo: String, bar: Int)

val reactClass = React.createClass[WrappedProps, Unit](
  render = (self) =>
    <.div()(
      s"foo: ${self.props.wrapped.foo}",
      <.br.empty,
      s"bar: ${self.props.wrapped.bar}"
    )
)

ReactDOM.render(
  <(reactClass)(^.wrapped := WrappedProps("foo", 123))(),
  mountNode
)
```

Props looks like the following:

```scala
case class Props[Wrapped](native: js.Dynamic) {
  def wrapped: Wrapped = native.wrapped.asInstanceOf[Wrapped]
  def children: ReactElement = native.children.asInstanceOf[ReactElement]
}
```

You can extend it as you see needs. See [`RouterProps`](/router/src/main/scala/io/github/shogowada/scalajs/reactjs/router/RouterProps.scala) for an example.

### How about states?

States are wrapped and unwrapped automatically, so you don't need to do `state.wrapped`. We can wrap and unwrap states automatically because nobody extends states.

```scala
case class State(text: String)

val reactClass = React.createClass[Unit, State](
  getInitialState = (self) => State(text = ""),
  render = (self) =>
    <.div()(
      <.input(
        ^.placeholder := "Type something here",
        ^.value := self.state.text,
        ^.onChange := onChange(self)
      )()
    )
)

def onChange(self: Self[Unit, State]): Unit = // Higher-order function
  (event: InputFormSyntheticEvent) => {
    val newText = event.target.value
    self.setState(_.copy(text = newText))
  }
```
