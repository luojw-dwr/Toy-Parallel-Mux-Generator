package muxgen

import chisel3._
import chisel3.iotesters
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class MuxgenUnitTester(c: MuxN[UInt]) extends PeekPokeTester(c) {
  private val DUT = c
  for(i <- 0 to 49) {
    poke(DUT.io.xs(i), i)
  }
  for(i <- 0 to 49) {
    poke(DUT.io.sel, i)
    step(1)
    expect(DUT.io.y, i)
  }
}


class MuxgenTester extends ChiselFlatSpec {
  "Basic test using Driver.execute" should "be used as an alternative way to run specification" in {
    iotesters.Driver.execute(Array(), () => new MuxN(UInt(16.W), 50)) {
      c => new MuxgenUnitTester(c)
    } should be (true)
  }
}
