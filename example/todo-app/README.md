# Tutorial: TODO App

In this tutorial, we will replicate [the TODO App application from the official React website](https://facebook.github.io/react/).

- [JSX vs. Scala](#jsx-vs-scala)
    - [JSX](#jsx)
    - [Scala](#scala)
- [Define a new React Component](#define-a-new-react-component)
- [Define props and state](#define-props-and-state)
- [Define an initial state](#define-an-initial-state)
- [Define render method](#define-render-method)
- [Update state](#update-state)
- [Mount it to DOM](#mount-it-to-dom)

## JSX vs. Scala

Before going into the details, let's see how each of them (JSX and Scala) look like.

### JSX

```jsx
class TodoApp extends React.Component {
  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.state = {items: [], text: ''};
  }

  render() {
    return (
      <div>
        <h3>TODO</h3>
        <TodoList items={this.state.items} />
        <form onSubmit={this.handleSubmit}>
          <input onChange={this.handleChange} value={this.state.text} />
          <button>{'Add #' + (this.state.items.length + 1)}</button>
        </form>
      </div>
    );
  }

  handleChange(e) {
    this.setState({text: e.target.value});
  }

  handleSubmit(e) {
    e.preventDefault();
    var newItem = {
      text: this.state.text,
      id: Date.now()
    };
    this.setState((prevState) => ({
      items: prevState.items.concat(newItem),
      text: ''
    }));
  }
}

class TodoList extends React.Component {
  render() {
    return (
      <ul>
        {this.props.items.map(item => (
          <li key={item.id}>{item.text}</li>
        ))}
      </ul>
    );
  }
}

ReactDOM.render(<TodoApp />, mountNode);
```

### Scala

```scala
case class Item(id: String, text: String)

class TodoApp extends ReactClassSpec {
  case class Props()
  case class State(items: Seq[Item], text: String)

  val todoList = new TodoList()

  override def getInitialState() = State(items = Seq(), text = "")

  override def render() = {
    <.div()(
      <.h3()("TODO"),
      <.reactElement(todoList, todoList.Props(items = state.items)),
      <.form(^.onSubmit := handleSubmit)(
        <.input(^.onChange := handleChange, ^.value := state.text)(),
        <.button()(s"Add #${state.items.size + 1}")
      )
    )
  }

  val handleChange = (event: InputElementSyntheticEvent) => {
    val newText = event.target.value
    setState(_.copy(text = newText))
  }

  val handleSubmit = (event: SyntheticEvent) => {
    event.preventDefault()
    val newItem = Item(text = state.text, id = js.Date.now().toString)
    setState((previousState: State) => State(
      items = previousState.items :+ newItem,
      text = ""
    ))
  }
}

class TodoList extends StatelessReactClassSpec {
  case class Props(items: Seq[Item])

  override def render() = <.ul()(props.items.map(item => <.li(^.key := item.id)(item.text)))
}

ReactDOM.render(new TodoApp(), mountNode)
```

## Define a new React Component

To define a new React Component, you need to create a new class extending ```ReactClassSpec``` or its subclasses.

```scala
class TodoApp extends ReactClassSpec
class TodoList extends StatelessReactClassSpec
````

Here, we are using 2 different specs. One is ```ReactClassSpec``` and the other one is ```StatelessReactClassSpec```.

```ReactClassSpec``` gives you full control, while ```StatelessReactClassSpec``` cannot have state.

## Define props and state

You need to define types of props and state for ```ReactClassSpec```, and you need to define type of props for ```StatelessReactClassSpec```.

You can do that by defining ```Props``` and ```State``` class inside your class spec.

```scala
class TodoApp extends ReactClassSpec {
  case class Props()
  case class State(items: Seq[Item], text: String)
}

class TodoList extends StatelessReactClassSpec {
  case class Props(items: Seq[Item])
}
```

If you want to declare your ```Props``` and ```State``` outside the class, you can do that too.

```scala
case class TodoAppProps()
case class TodoAppState(items: Seq[Item], text: String)
case class TodoListProps(items: Seq[Item])

class TodoApp extends ReactClassSpec {
  type Props = TodoAppProps
  type State = TodoAppState
}

class TodoList extends StatelessReactClassSpec {
  type Props = TodoListProps
}
```

## Define an initial state

If your class spec is stateful, you need to give it an initial state too. You don't need to do this for ```StatelessReactClassSpec```.

```scala
class TodoApp extends ReactClassSpec {
  case class State(items: Seq[Item], text: String)

  override def getInitialState() = State(items = Seq(), text = "")
}
```

## Define render method

You can render your virual DOMs using ```VirtualDOM``` class. To use it, ```import io.github.shogowada.scalajs.reactjs.VirtualDOM._``` first, then start writing tags with ```<``` and attributes with ```^```.

```scala
import io.github.shogowada.scalajs.reactjs.VirtualDOM._

class TodoApp extends ReactClassSpec {
  val todoList = new TodoList()

  override def render() = {
    <.div()(
      <.h3()("TODO"),
      <.reactElement(todoList, todoList.Props(items = state.items)),
      <.form(^.onSubmit := handleSubmit)(
        <.input(^.onChange := handleChange, ^.value := state.text)(),
        <.button()(s"Add #${state.items.size + 1}")
      )
    )
  }
}

class TodoList extends StatelessReactClassSpec {
  override def render() = <.ul()(props.items.map(item => <.li(^.key := item.id)(item.text)))
}
```

Note that you can render other React components by using ```<.reactElement(ReactClassSpec, ReactClassSpec#Props)``` tag.

## Update state

If your component is stateful, you will often update your state. You can do so by using ```setState``` method.

```scala
class TodoApp extends ReactClassSpec {
  case class State(items: Seq[Item], text: String)

  val handleChange = (event: InputElementSyntheticEvent) => {
    val newText = event.target.value
    setState(_.copy(text = newText))
  }

  val handleSubmit = (event: SyntheticEvent) => {
    event.preventDefault()
    val newItem = Item(text = state.text, id = js.Date.now().toString)
    setState((previousState: State) => State(
      items = previousState.items :+ newItem,
      text = ""
    ))
  }
}
```

When updating state, unlike you are overriding the whole state, you need to copy the previous state to generate a new state. This is because state update is async process, and you cannot partially update the state unlike JavaScript.

For example, the following might cause race condition and override ```items``` value with old one unexpectedly.

```scala
setState(state.copy(text = event.target.value))
```

So it needs to be the following instead:

```scala
val newText = event.target.value
setState((previousState: State) => previousState.copy(text = newText))
```

Or, to make it less verbose:

```scala
val newText = event.target.value
setState(_.copy(text = newText))
```

If the new state depends on the current state, you can use the previous state to generate the new state.

```scala
setState((previousState: State) => State(
  items = previousState.items :+ newItem,
  text = ""
))
```

## Mount it to DOM

Finally, you can mount your React component to DOM.

```scala
ReactDOM.render(new TodoApp(), mountNode)
```
