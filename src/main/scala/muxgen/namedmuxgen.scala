package muxgen

import chisel3._
import chisel3.util.log2Up

class NamedMuxN[T <: Data, NNat <: Nat](private val gen: T, val nat: NNat) extends Module {
    val n = nat.asInt
    assert(n > 1)
    override def desiredName = s"NamedMux${n.toString}"
    val io = IO(MuxIO(gen, n))
    val selWidth = io.sel.getWidth
    val xss = NamedMuxN.decomposeXs(io.xs)
    val ys = xss.map((xs: Seq[T]) => NamedMuxN(gen, io.sel(selWidth - 2, 0), xs))
    io.y := Mux2(gen, io.sel(selWidth - 1), ys)
}

object NamedMuxN {
    def decomposeXs[T <: Data](xs: Seq[T]) = {
        val n = xs.length
        val nL = (1 << (log2Up(n) - 1))
        xs.grouped(nL).toSeq
    }
    def apply[T <: Data](gen: T, sel: UInt, xs: Seq[T]): T = {
        val n = xs.length
        assert(n > 0)
        n match {
            case 1 => xs(0)
            case 2 => Mux2(gen, sel, xs)
            case _ => {
                val m = Module(new NamedMuxN(gen, Nat(n))).io
                m.sel := sel
                m.xs := xs
                m.y
            }
        }
    }
}
