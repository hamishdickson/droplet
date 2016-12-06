package lens

case class Lens[A, B](
  g: A => B,
  s: (B, A) => A
) {
  def get(a: A): B = g(a)
  def set(b: B, a: A): A = s(b, a)
}
