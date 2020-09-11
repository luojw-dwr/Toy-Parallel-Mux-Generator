package muxgen

/*
* Representation of natural numbers in type system.
* For a given Int n >= 0, use Nat(n) to get Nat.
* For a given Nat nat, use nat.asInt to get Int.
*/

sealed trait Nat {
    val asInt: Int
    def succ: Nat
}

case class SuccNat[nNat <: Nat](nat: nNat) extends Nat {
    val asInt = 1 + nat.asInt
    def succ = new SuccNat(this)
}
case object ZeroNat extends Nat {
    val asInt = 0
    def succ = new SuccNat(this)
}

object Nat {
    val zero = ZeroNat
    def apply(n: Int): Nat = {
        assert(n >= 0)
        n match {
            case 0 => zero
            case _ => Nat(n - 1).succ
        }
    }
}
