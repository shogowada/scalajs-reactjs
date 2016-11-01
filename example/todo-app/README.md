# Tutorial: TODO App

In this tutorial, we will replicate [the TODO App application from the official React website](https://facebook.github.io/react/).

Before going into the details, let's see how each of them look like.

Here is the JSX version of the code:

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

And here is the Scala version:

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
    setState(state.copy(text = event.target.value))
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

ReactDOM.render(new TodoApp(), element)
```

Let's get into the details.

## Define a new React Component

To define a new React Component, you need to create a new class extending ```ReactClassSpec``` or its subclasses.

```scala
class TodoApp extends ReactClassSpec
class TodoList extends StatelessReactClassSpec
````

Here, we are using 2 different specs. One is ```ReactClassSpec``` and the other one is ```StatelessReactClassSpec```.

```ReactClassSpec``` gives you full control, while ```StatelessReactClassSpec``` cannot have state.

## Define props and state

You need to define types of props and state for ```ReactClassSpec```.

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

If you want to define your Props and State outside the class, you can do that too.

```scala
case class TodoAppProps()
case class TodoAppState(items: Seq[Item], text: String)

class TodoApp extends ReactClassSpec {
  type Props = TodoAppProps
  type State = TodoAppState
}

case class TodoListProps(items: Seq[Item])

class TodoList extends StatelessReactClassSpec {
  type Props = TodoListProps
}
```
