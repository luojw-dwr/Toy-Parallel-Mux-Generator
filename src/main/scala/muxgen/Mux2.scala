package muxgen

import chisel3._
import chisel3.util.log2Up

// Comment the following class to make Mux2 a module.
// Uncomment the following class to make Mux2 a black box.
/*
class Mux2[T <: Data](private val gen: T) extends BlackBox {
    val io = IO(MuxIO(gen, 2))
    io.y := Mux(io.sel === 1.U, io.xs(1), io.xs(0))
}
*/


// Comment the following class to make Mux2 a balck box.
// Uncomment the following class to make Mux2 a module.
class Mux2[T <: Data](private val gen: T) extends Module {
    val io = IO(MuxIO(gen, 2))
    io.y := Mux(io.sel === 1.U, io.xs(1), io.xs(0))
}

object Mux2 {
    def apply[T <: Data](gen: T, sel: UInt, xs: Seq[T]) = {
        val mux2 = Module(new Mux2(gen)).io
        mux2.sel := sel
        mux2.xs  := xs
        mux2.y
    }
}
