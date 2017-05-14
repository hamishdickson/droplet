package droplet

import lens._
import laws.LensLaws._
import cats._
import cats.data._
import cats.implicits._
import org.scalacheck.Gen
import org.scalacheck.Prop.forAll
import org.scalatest.{FlatSpec, Matchers}

class LensTests extends FlatSpec with Matchers {
  import LensTests._

  "A lens" should "hold the getandsetlaw" in {
    val test = forAll(fooGen, sGen) { (foo, s) =>
      getAndSetLaw(fooSLens, foo, s)
    }

    test.check
  }

  it should "hold the compositionlaw" in {
    val test = forAll(fooGen, barGen, sGen) { (foo, bar, s) =>
      compositionLaw(barFLens, barSLens, bar, foo, s)
    }

    test.check
  }

  it should "hold the setAndGetLaw" in {
    val test = forAll(fooGen) { foo =>
      setAndGetLaw(Lens.identityLens[Foo], foo)
    }

    test.check
  }
}

object LensTests {
  case class Foo(s: String, i: Int)
  case class Bar(d: Double, f: Foo)

  implicit val fooEq = new Eq[Foo] {
    def eqv(a: Foo, b: Foo): Boolean = a.s === b.s && a.i === b.i
  }

  implicit val barEq = new Eq[Bar] {
    def eqv(a: Bar, b: Bar): Boolean = a.d === b.d && a.f === b.f
  }

  val fooGen = for {
    s <- Gen.alphaStr
    i <- Gen.choose(-100, 100)
  } yield Foo(s, i)

  val barGen = for {
    f <- fooGen
    d <- Gen.choose(-100.0, 100.0)
  } yield Bar(d, f)

  val sGen = Gen.alphaStr

  val fooSLens = Lens[Foo, String] (
    f => f.s,
    (s, f) => Foo(s, f.i)
  )

  val barFLens = Lens[Bar, Foo] (
    b => b.f,
    (f, b) => Bar(b.d, f)
  )

  val barSLens = Lens[Bar, String] (
    b => b.f.s,
    (s, b) => Bar(b.d, Foo(s, b.f.i))
  )
}
