Data Uncertainty Propagation in smart grid - Pure Java - Benchmark
---

# How to compile (?)

- You should first clone and install the `sntcreos/ducpropagationjava` project
    - `mvn clean install` from `sntcreos/ducpropagationjava` cloned folder
- Generate the jar file of the benchmark project:
    - `mvn clean install` from the root folder of this project

# Run benchmarks:

Suggested command line: `java -jar <PATH>/approximator-bench.jar duc.propagation.bench -foe true -gc true -rff results.csv`

- `duc.propagation.bench`: run all benchmarks that are in the `duc.propagation.bench`Java package
- `-foe true`: failed any error is encountered during the execution
- `-gc true`: forces GC between each execution
- `-rff results.csv`: export results in the `results.csv` CSV file

# Benchmarks

## duc.propagation.ignoredbench.*

Old or benchmarks used for manual testing. As suggested by their names, they can be ignored.

## duc.propagation.bench.*

Here to quantify the loss in performances of uncertain load approximation.

### 2020.04.0D-SNAPSHOT

- Benchmarks are executed over 3 simple topologies, that come from the different scenarios: parallel cables at the transformer (ParaTransformer*), parallel cables at a cabinet (ParaCabinet*), and indirect parallel cables (IndirectPara)
- Variables changed among the different executions: the number of uncertain fuses, for 0 to all of them
- All executions will be compared to the "reference" (children of GenReference) benchmark, i.e., execution of the certain load approximation
   