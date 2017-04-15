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

## How to create React components?

To create React components, extend `ReactClassSpec[WrappedProps, State]` or one of its sub classes.

You have four options:

- `ReactClassSpec[WrappedProps, State]`
    - This is the parent of them all. You can have both custom props and state.
- `StatelessReactClassSpec[WrappedProps]`
    - You cannot have state.
- `PropslessReactClassSpec[State]`
    - You cannot have custom props.
- `StaticReactClassSpec`
    - You cannot have both custom props and state.

To render React components, use `<(/* React component */)` to make it an element. You can pass attributes and children like regular elements.

```scala
import io.github.shogowada.scalajs.reactjs.VirtualDOM._

class MyComponent extends ReactClassSpec[WrappedProps, State] {
  // ...
}

ReactDOM.render(
  <(new MyComponent())(/* attributes (a.k.a. props) */)(/* children */),
  mountNode
)
```

### What's WrappedProps?

While many want to use case classes as props, React requires props to be a plain JavaScript object. So, to use case classes, we need to wrap the case class in another property. In this facade, we wrap it in "wrapped" property.

```scala
case class WrappedProps(foo: String, bar: Int)

class MyComponent extends StatelessReactClassSpec[WrappedProps] {
  override def render(): ReactElement =
    <.div()(
      s"foo: ${props.wrapped.foo}",
      <.br.empty,
      s"bar: ${props.wrapped.bar}"
    )
}

ReactDOM.render(
  <(new MyComponent())(^.wrapped := WrappedProps("foo", 123))(),
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

You can extend it as you see needs. See [`RouterProps`](/router/src/main/scala/io/github/shogowada/scalajs/reactjs/router/RouterProps) for an example.

### How about states?

States are wrapped and unwrapped automatically, so you don't need to do `state.wrapped`. We can wrap and unwrap states automatically because nobody extends states.

```scala
case class State(text: String)

class MyComponent extends PropslessReactClassSpec[State] {
  override def getInitialState(): State = State(text = "")

  override def render(): ReactElement =
    <.div()(
      <.input(
        ^.placeholder := "Type something here",
        ^.value := state.text,
        ^.onChange := onChange
      )()
    )

  val onChange = (event: InputFormSyntheticEvent) => {
    val newText = event.target.value
    setState(_.copy(text = newText))
  }
}
```
