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
  def getAndSetLaw[A, B](l: Lens[A, B], a: A, b: B)(implicit eq: Eq[B]) =
    l.get(l.set(b, a)) === b

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

  def setAndGetLaw[A](l: Lens[A, A], a: A)(implicit eq: Eq[A]) =
    l.set(l.get(a), a) === a
}

