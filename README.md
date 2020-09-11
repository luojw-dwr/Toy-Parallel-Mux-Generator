Toy Parallel Mux Generator
=======================

Given a `Mux2` module, this project offers a toy model as a parallel mux generator for any number of inputs. 

## Module Hierarchy

There are two styles of module hierarchy is provided:

* `MuxN[T <: Data](gen: T, n: Int)` generates a single module containing `Mux2`s and the wires. With this style, muxes with different types of inputs are differed at the type level while muxes with different numbers of inuts are only differed at the instance level.
* `NamedMuxN[T <: Data, NNat <: Nat](gen: T, nat: NNat)` generates the involved muxes whose numbers of inputs are powers of 2 and the final output with the desired number of inputs. With this style, muxes with different number of inputs or different types of inputs are differed at the type level.

## Features

As an experimental toy project, a few additional features are added to some software guy's concerns:

* Generic type: Both `MuxN` and `NamedMuxN` supports generic types of inputs.
* Type safety: Experimentally, `NamedMuxN` shows how to differ muxes with different numbers of inputs at type level. (Maybe an overkill.)

## Usage

### Emit Verilog

After modifying `src/main/scala/muxgen/main.scala` for your desired number of inputs, change your directory to the root directory of the project and run the following in the shell:

```bash
$ make build
```

Then the generated annotations, firrtl files and Verilog files should lie in `build/`.

## Run Unit Test

*Before running the test, modify the `Mux2` class in `src/main/scala/muxgen/Mux2.scala` such that the class should be derived from `Module`  with its inner logic rather than a `BlackBox`.*

Run the following in the shell:

```bash
$ make test
```

