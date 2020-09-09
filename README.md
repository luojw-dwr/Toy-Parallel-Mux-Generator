Toy Parallel Mux Generator
=======================

Given a `Mux2` module, this project offers a toy model as a parallel mux generator for any number of inputs.

## Usage

### Emit Verilog

After modifying `src/main/scala/muxgen/main.scala` for your desired number of inputs, change your directory to the root directory of the project and run the following in the shell:

```bash
$ make build
```

Then the generated annotations, firrtl files and Verilog files should lie in `build/`.

## Run Unit Test

*Before run the test, modify the `Mux2` class in `src/main/scala/muxgen/muxgen.scala` such that the class should be derived from `Module`  with the inner logic rather than a `BlackBox`.*

Run the following in the shell:

```bash
$ make test
```

