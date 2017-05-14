package droplet
package laws

import lens._

import cats._
import cats.data._
import cats.implicits._

/*
 * Quote Ed Kmett
 *
 * for some lens l
 *
 * l.get(l.set(b, a)) === b
 * l.set(c, l.set(b, a)) === l.set(c, a)
 * l.set(l.get(a), a) === a
 */
trait LensLaws {
  /**
    * If I set some field in a to b, then I get that field, I should get b
    */
  def getAndSetLaw[A, B](l: Lens[A, B], a: A, b: B)(implicit eq: Eq[B]) =
    l.get(l.set(b, a)) === b

  /**
    * If I set some field and then set it again, I should be able to get the latest thing
    */
  def compositionLaw[A, B, C](
    lab: Lens[A, B],
    lac: Lens[A, C],
    a: A,
    b: B,
    c: C
  )(
    implicit eq: Eq[A]
  ) =
    lac.set(c, lab.set(b, a)) === lac.set(c, a)

  /**
    * If I set the value a and then get it back, then I don't change the meaning of a
    */
  def setAndGetLaw[A](l: Lens[A, A], a: A)(implicit eq: Eq[A]) =
    l.set(l.get(a), a) === a
}

object LensLaws extends LensLaws
