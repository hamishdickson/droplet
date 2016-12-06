package laws

import org.scalacheck.Prop
import org.scalatest.{Matchers, FlatSpec}

class LensTest extends FlatSpec with Matchers {
  "A lens" should "obey the lens laws" in {
    /*
     * for some lens l
     *
     * l.get(l.set(b, a)) === b
     * l.set(c, l.set(b, a)) === l.set(c, a)
     * l.set(l.get(a), a) === a
     */
  }
}
