package droplet
package lens

import cats._
import cats.data._
import cats.implicits._

/**
  * Lens
  *
  * Given an `A` and a value `B` a `Lens` will let you
  *
  *  - get: Given an `A`, get a `B` out of it
  *
  *  - set: take an `A` and a `B` you want to set into it, and build you a new `A`
  *
  * Examples:
  *
  *  {{{
     scala> import droplet.lens._
     import droplet.lens._

     scala> case class Foo(s: String, i: Int)
     defined class Foo

     scala> val fooSLens = Lens[Foo, String] (
     | (f: Foo) => f.s,
     | (s: String, f: Foo) => Foo(s, f.i)
     | )
     fooSLens: droplet.lens.Lens[Foo,String] = Lens($$Lambda$3862/2088200542@567fa392,$$Lambda$3863/1495144578@7afe75b9)

     scala> val myFoo = Foo("Hello", 1)
     myFoo: Foo = Foo(Hello,1)

     scala> fooSLens.get(myFoo)
     res0: String = Hello

     scala> fooSLens.set("World!", myFoo)
     res1: Foo = Foo(World!,1)
  *  }}}
  *
  */
case class Lens[A, B](
  // given an A get a B out of it
  g: A => B,
  // take an A and a B you want to set into it, and build a new A
  s: (B, A) => A
) {
  def get(a: A): B = g(a)
  def set(b: B, a: A): A = s(b, a)

  // modify a value
  def mod(f: B => B, a: A): A =
    set(f(get(a)), a)

  /**
    * composition
    * if I have a lens that knows how to get from A => B and a lens that knows how to
    * get from B => C, then I can build a lens from A => C
    */
  def andThen[C](l: Lens[B, C]) = Lens[A,C](
    (a: A) => l.get(get(a)),
    (c: C, a: A) => mod(b => l.set(c, b), a)
  )

  def >>>[C](l: Lens[B, C]) = andThen[C](l)

  def compose[C](that: Lens[C, A]) =
    that >>> this

  def <<<[C](that: Lens[C, A]) =
    compose[C](that)

  def := (b: B) = State[A, B]{ a =>
    (set(b, a), b)
  }

  // POW
  implicit def lensAsState[S, A](l: Lens[S, A]) = State[S, A] { s =>
    (s, l.get(s))
  }

  def %= (f: B => B): State[A, B] = for {
    x <- this
    y <- this := f(x)
  } yield y
}

object Lens {
  def identityLens[A] = Lens[A, A] (
    a => a,
    (_, a) => a
  )
}
