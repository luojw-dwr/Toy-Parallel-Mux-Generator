package muxgen

import chisel3._
import chisel3.util.log2Up

class MuxIO[T <: Data](private val gen: T, val n: Int) extends Bundle {
    val sel = Input(UInt(log2Up(n).W))
    val xs  = Input(Vec(n, gen.cloneType))
    val y   = Output(gen.cloneType)
}

object MuxIO {
    def apply[T <: Data](gen: T, n: Int) = {
        new MuxIO(gen, n)
    }
}
