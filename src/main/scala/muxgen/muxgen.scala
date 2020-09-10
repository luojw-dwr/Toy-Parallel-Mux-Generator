package muxgen

import chisel3._
import chisel3.util.log2Up

object MuxPow2 {
    def apply[T <: Data](gen: T, sel: UInt, xs: Seq[T]): T = {
        val rank = sel.getWidth
        val n = 1 << rank
        assert(rank > 0)
        if (rank == 1) {
            Mux2(gen, sel, xs)
        } else {
            val sel_MSB = sel(rank - 1)
            val sel_LBS = sel(rank - 2, 0)
            val xsG = ((n >> 1) to (n - 1)).map((idx: Int) => xs(idx))
            val xsL = (0 to ((n >> 1) - 1)).map((idx: Int) => xs(idx))
            val muxG = MuxPow2(gen, sel_LBS, xsG)
            val muxL = MuxPow2(gen, sel_LBS, xsL)
            Mux2(gen, sel_MSB, VecInit(muxL, muxG))
        }
    }
}

object MuxAny {
    def apply[T <: Data](gen: T, n: Int, sel: UInt, xs: Seq[T]): T = {
        assert(n > 1)
        val rankCeil = log2Up(n)
        assert(sel.getWidth >= rankCeil)
        val is_n_pow2 = (1 << rankCeil) == n
        if (is_n_pow2) {
            MuxPow2(gen, sel, xs)
        } else {
            val rankFloor = rankCeil - 1
            val nL = 1 << rankFloor
            val nG = n - nL
            val sel_MSB = sel(rankCeil - 1)
            val sel_LBG = sel(log2Up(nG) - 1, 0)
            val sel_LBL = sel(rankFloor - 1, 0)
            val xsG = (nL to (n - 1)).map((idx: Int) => xs(idx))
            val xsL = (0 to (nL - 1)).map((idx: Int) => xs(idx))
            val yG = if (nG == 1) {
                xs(n - 1)
            } else {
                MuxAny(gen, nG, sel_LBG, xsG)
            }
            val yL = MuxAny(gen, nL, sel_LBL, xsL)
            Mux2(gen, sel_MSB, VecInit(yL, yG))
        }
    }
}

class MuxN[T <: Data](private val gen: T, val n: Int) extends Module {
    assert(n > 1)
    val io = IO(MuxIO(gen, n))
    io.y := MuxAny(gen, n, io.sel, io.xs)
}
