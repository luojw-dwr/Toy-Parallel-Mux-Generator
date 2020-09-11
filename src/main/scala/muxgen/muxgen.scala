package muxgen

import chisel3._
import chisel3.util.log2Up

/*
* Method object to generate a mux with its number of inputs a power of 2.
*/
object MuxPow2 {
    /*
    * Returns the output of the output of the mux.
    * The number of inputs should be a power of 2.
    * @T:   input type
    * @gen: indicator of input data type
    * @sel: selection signal
    * @xs:  vector of inputs
    */
    def apply[T <: Data](gen: T, sel: UInt, xs: Seq[T]): T = {
        val rank = sel.getWidth
        val n = 1 << rank
        assert(rank > 0)
        if (rank == 1) {
            Mux2(gen, sel, xs)
        } else {
            // sel for the final input
            val sel_MSB = sel(rank - 1)
            // sel for the smaller muxes
            val sel_LBS = sel(rank - 2, 0)
            // inputs within high range
            val xsG = ((n >> 1) until n).map((idx: Int) => xs(idx))
            // inputs within low range
            val xsL = (0 until (n >> 1)).map((idx: Int) => xs(idx))
            // smaller mux for high range
            val muxG = MuxPow2(gen, sel_LBS, xsG)
            // smaller mux for low range
            val muxL = MuxPow2(gen, sel_LBS, xsL)
            // mux2 for the final output
            Mux2(gen, sel_MSB, VecInit(muxL, muxG))
        }
    }
}

/*
* Method object to generate a mux with its number of inputs larger than 1.
*/
object MuxAny {
    /*
    * Returns the output of the mux.
    * The number of inputs should be larger than 1.
    * @T:   input type
    * @gen: indicator of input data type
    * @sel: selection signal
    * @xs:  vector of inputs
    */
    def apply[T <: Data](gen: T, sel: UInt, xs: Seq[T]): T = {
        val n = xs.length
        assert(n > 1)
        val rankCeil = log2Up(n)
        assert(sel.getWidth >= rankCeil)
        val is_n_pow2 = (1 << rankCeil) == n
        if (is_n_pow2) {
            MuxPow2(gen, sel, xs)
        } else {
            val rankFloor = rankCeil - 1
            // number of inputs which lie in low range
            val nL = 1 << rankFloor
            // number of inputs which lie in high range
            val nG = n - nL
            // sel for the final input
            val sel_MSB = sel(rankCeil - 1)
            // sel for low range
            val sel_LBG = sel(log2Up(nG) - 1, 0)
            // sel for high range
            val sel_LBL = sel(rankFloor - 1, 0)
            // inputs within high range
            val xsG = (nL until n).map((idx: Int) => xs(idx))
            // inputs within low range
            val xsL = (0 until nL).map((idx: Int) => xs(idx))
            // use the input directly if only one input in high range,
            // otherwise generates a smaller mux for high range
            val yG = if (nG == 1) {
                // when the number of inputs in high range is 1,
                // use the input directly and finish recursion
                xs(n - 1)
            } else {
                MuxAny(gen, sel_LBG, xsG)
            }
            // smaller mux for low range
            val yL = MuxAny(gen, sel_LBL, xsL)
            // mux2 for the final output
            Mux2(gen, sel_MSB, VecInit(yL, yG))
        }
    }
}


/*
* Mux module for any given number of inputs
* @T:   input type
* @gen: indicator of input data type
* @n:   number of inputs
* @io:  MuxIO[T](n)
*/
class MuxN[T <: Data](private val gen: T, val n: Int) extends Module {
    assert(n > 1)
    override def desiredName = s"Mux${n.toString}"
    val io = IO(MuxIO(gen, n))
    io.y := MuxAny(gen, io.sel, io.xs)
}
