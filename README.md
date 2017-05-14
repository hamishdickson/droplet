# droplet

## Overview

droplet is a tiny and simple lens library, based wholey on [this talk by Ed Kmett](https://www.youtube.com/watch?v=efv0SQNde5Q).

This is a really just for my own enjoyment, but if you find it useful, go ahead and use what you like from it

## Examples


```scala
import droplet.lens._

def trivial[A] = Lens[A, Unit](
  a => (),
  (_, a) => a
)

def self[A] = Lens[A, A] (
  a => a,
  (a, _) => a
)

def fst[A, B] = Lens[(A, B), A](
  p => p._1,
  (a, p) => p.copy(_1 = a)
)

def snd[A, B] = Lens[(A, B), B](
  p => p._2,
  (b, p) => p.copy(_2 = b)
)

foo = (1, (2, 3))

bar = snd.andThen(snd).set(foo, 4)
```

Some more

Set lens

```scala
import droplet.lens._
import scala.collections.immutable.Set

def contains[K](k: K) =
  Lens[Set[K], Boolean](
    s => s contains k,
    {
      case (True, s) => s + k
      case (False, s) => s - k // !!!
    }
  )

foo = Set(1, 2, 3)
contains(2).get(foo)        // True
contains(4).set(foo, True)  // Set(1,2,3,4)
contains(3).set(foo, False) // Set(1,2)
```

Map lens

```scala
import droplet.lens._
import scala.collections.immutable.Map

def member[K, V](k: K) =
  Lens[Map[K], Option[V]](
    m => m get k,
    {
      case (Some(v), m) => m + (k -> v)
      case (None, m) => m - k // !!!
    }
  )

foo = Map(1 -> "A", 2 -> "B")
member(2).get(foo) // Some("B")
member(2).set(foo, None) // Map(1 -> "A")
member(2).set(foo, Some("C")) // Map(1 -> "A", 2 -> "C")
```
