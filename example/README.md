# Basics

This is just a facade for React, so if you are not already familiar with React, I recommend [getting familiar with React first](https://facebook.github.io/react/tutorial/tutorial.html).

- [How does it replace JSX in Scala?](#how-does-it-replace-jsx-in-scala)
- [How to create React classes?](#how-to-create-react-classes)
- [What's WrappedProps?](#whats-wrappedprops)
- [How about states?](#how-about-states)
- [Can I see fully working examples?](#can-i-see-fully-working-examples)

## How does it replace JSX in Scala?

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

If `<` and `^` look weird to you, you can [change it](/example/custom-virtual-dom), but otherwise think of `<` as opening a tag (`<` of `<div>`) and `^` as attaching an attribute to the tag.

You can also get rid of the prefixes altogether by importing `<._` and `^._`, but it is not recommended because many HTML tag and attribute names are concise and tend to conflict with other variable names (e.g. `id` attribute, `map` tag).

Visit [the Static Tags page](https://github.com/shogowada/statictags) for more techniques (e.g. using dynamic tags and attributes, flattening tags and attributes).

## How to create React classes?

Use `React.createClass[WrappedProps, State]` to create React classes (we will explain about `WrappedProps` in next section).

```scala
val reactClass: ReactClass = React.createClass[Unit, Unit]( // If you don't have props or state, use Unit.
  (self) => <.div()("Hello, World!")
)
```

In addition to `render` function, it also supports [all the functions `React.Component` supports](https://facebook.github.io/react/docs/react-component.html) except `defaultProps`.

The first argument of each function must be `Self[WrappedProps, State]`, props must have type `Props[WrappedProps]`, and states must have type `State`.

For example, [`componentWillUpdate(nextProps, nextState)` function](https://facebook.github.io/react/docs/react-component.html#componentwillupdate) will be `componentWillUpdate(self: Self[WrappedProps, State], nextProps: Props[WrappedProps], nextState: State): Unit`:

```scala
case class WrappedProps(/* ... */)
case class State(/* ... */)

type Self = React.Self[WrappedProps, State]
type Props = React.Props[WrappedProps]

val reactClass: ReactClass = React.createClass[WrappedProps, State](
  componentWillUpdate = (self: Self, nextProps: Props, nextState: State) => {/* do something */},
  render = (self: Self) => <.div()("Hello, World!")
)
```

`Self[WrappedProps, State]` is equivalent of `this` in React, so you can do things like `self.props.children` or `self.setState(State(foo = "bar"))`. Unlike React, we are just giving it as input of the functions rather than class members.

To render React classes, use `<(/* ReactClass */)` to make it a virtual DOM. You can pass attributes and children like regular elements.

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
  (self) =>
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

States are wrapped and unwrapped automatically, so you don't need to do `state.wrapped`. We can wrap and unwrap states automatically because nothing extends states; it is supposed to be local to each component.

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

def onChange(self: Self[Unit, State]) = // Use a higher-order function (a function returning a function)
  (event: FormSyntheticEvent[HTMLInputElement]) => {
    val newText = event.target.value // Cache the value because React reuses events
    self.setState(_.copy(text = newText)) // Shortcut for self.setState((prevState: State) => prevState.copy(text = newText))
  }
```

## Can I see fully working examples?

Sure, you can! All the projects in this folder are fully working examples, but here are some top picks:

- [TODO App](/example/todo-app/src/main/scala/io/github/shogowada/scalajs/reactjs/example/todoapp/Main.scala)
- [Routing](/example/routing/src/main/scala/io/github/shogowada/scalajs/reactjs/example/routing/Main.scala)
- [Redux](/example/todo-app-redux/src/main/scala/io/github/shogowada/scalajs/reactjs/example/todoappredux)
- [Redux DevTools](/example/redux-devtools/src/main/scala/io/github/shogowada/scalajs/reactjs/example/redux/devtools/Main.scala)
- [Redux Middleware](./example/redux-devtools/src/main/scala/io/github/shogowada/scalajs/reactjs/example/redux/middleware/Main.scala)
