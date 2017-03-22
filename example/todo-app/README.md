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

object TodoApp {
  case class State(items: Seq[Item], text: String)

  def apply() = new TodoApp()
}

class TodoApp extends PropslessReactClassSpec[TodoApp.State] {
  import TodoApp._

  override def getInitialState() = State(items = Seq(), text = "")

  override def render(): ReactElement = {
    <.div()(
      <.h3()("TODO"),
      TodoList(TodoList.Props(items = state.items)),
      <.form(^.onSubmit := handleSubmit)(
        <.input(^.onChange := handleChange, ^.value := state.text)(),
        <.button()(s"Add #${state.items.size + 1}")
      )
    )
  }

  private val handleChange = (e: InputFormSyntheticEvent) => {
    val newText = e.target.value
    setState(_.copy(text = newText))
  }

  private val handleSubmit = (e: SyntheticEvent) => {
    e.preventDefault()
    val newItem = Item(text = state.text, id = js.Date.now().toString)
    setState((prevState: State) => State(
      items = prevState.items :+ newItem,
      text = ""
    ))
  }
}

object TodoList {
  case class Props(items: Seq[Item])

  def apply(props: Props): ReactElement = (new TodoList) (props)()
}

class TodoList extends StatelessReactClassSpec[TodoList.Props] {
  override def render(): ReactElement = <.ul()(props.items.map(item => <.li(^.key := item.id)(item.text)))
}

val mountNode = dom.document.getElementById("mount-node")
ReactDOM.render(TodoApp(), mountNode)
```

## Define a new React Component

To define a new React Component, you need to create a new class extending ```ReactClassSpec``` or its subclasses.

```scala
class TodoApp extends PropslessReactClassSpec[TodoApp.State]
class TodoList extends StatelessReactClassSpec[TodoList.Props]

object TodoApp {
  case class State(items: Seq[Item], text: String)
}

object TodoList {
  case class Props(items: Seq[Item])
}
````

## Define an initial state

If your class spec is stateful, you need to give it an initial state too. You don't need to do this for ```StatelessReactClassSpec```.

```scala
class TodoApp extends PropslessReactClassSpec[TodoApp.State] {
  import TodoApp._

  override def getInitialState() = State(items = Seq(), text = "")
}
```

## Define render method

You can render your virual DOMs using ```VirtualDOM``` class. To use it, ```import io.github.shogowada.scalajs.reactjs.VirtualDOM._``` first, then start writing tags with ```<``` and attributes with ```^```.

```scala
import io.github.shogowada.scalajs.reactjs.VirtualDOM._

class TodoApp extends PropslessReactClassSpec[TodoApp.State] {
  override def render(): ReactElement = {
    <.div()(
      <.h3()("TODO"),
      TodoList(TodoList.Props(items = state.items)),
      <.form(^.onSubmit := handleSubmit)(
        <.input(^.onChange := handleChange, ^.value := state.text)(),
        <.button()(s"Add #${state.items.size + 1}")
      )
    )
  }
}

class TodoList extends StatelessReactClassSpec[TodoList.Props] {
  override def render(): ReactElement = <.ul()(props.items.map(item => <.li(^.key := item.id)(item.text)))
}
```

Note that you could render other React components by doing ```new TodoList()(TodoList.Props(items = state.items))```. This is because all the React component has ```apply(props: Props): ReactElement = React.createElement(this, props)``` method.

If the component doesn't have props, you can just do ```new TodoList()``` then it will be later converted to ```React.createElement(new TodoList())```.

## Update state

If your component is stateful, you can update the state by using ```setState``` method.

```scala

class TodoApp extends PropslessReactClassSpec[TodoApp.State] {
  import TodoApp._

  private val handleChange = (e: InputFormSyntheticEvent) => {
    val newText = e.target.value
    setState(_.copy(text = newText))
  }

  private val handleSubmit = (e: SyntheticEvent) => {
    e.preventDefault()
    val newItem = Item(text = state.text, id = js.Date.now().toString)
    setState((prevState: State) => State(
      items = prevState.items :+ newItem,
      text = ""
    ))
  }
}
```

Unless you are overriding the whole state, you need to copy the previous state to generate a new state. This is because state update is async process, and you cannot partially update the state unlike JavaScript.

For example, the following might cause race condition and override ```items``` value with old one unexpectedly.

```scala
setState(state.copy(text = e.target.value))
```

So it needs to be the following instead:

```scala
val newText = e.target.value
setState((prevState: State) => prevState.copy(text = newText))
```

Or, to make it less verbose:

```scala
val newText = e.target.value
setState(_.copy(text = newText))
```

If the new state depends on the current state, you can use the previous state to generate the new state.

```scala
setState((prevState: State) => State(
  items = prevState.items :+ newItem,
  text = ""
))
```

## Mount it to DOM

Finally, you can mount your React component to DOM.

```scala
ReactDOM.render(TodoApp(), mountNode)
```
