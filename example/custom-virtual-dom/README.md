# Custom Virtual DOM Example

If you want to customize `VirtualDOM`, you can create your own `VirtualDOM`.

For example, say you didn't like using `<` and `^` to access tags and attributes, and you wanted to use `E` and `A` instead. You can create your own `VirtualDOM` like

```scala
package io.github.shogowada.scalajs.reactjs.example.customvirtualdom

import io.github.shogowada.scalajs.reactjs.VirtualDOM

object CustomVirtualDOM extends VirtualDOM {
  lazy val E = <
  lazy val A = ^
}
```

then you can import the customized version instead of importing `VirtualDOM`:

```scala
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.example.customvirtualdom.CustomVirtualDOM._

object HelloWorld {
  def apply(): ReactElement = E.div(A.id := "hello-world")("Hello, World!")
}
```

`VirtualDOM` is just an extension of [`StaticTags`](https://github.com/shogowada/statictags). If you are interested in customizing `VirtualDOM` more, please read [`StaticTags`](https://github.com/shogowada/statictags) documentation.
