Data Uncertainty Propagation in smart grid - Pure Java - Benchmark
---

# How to compile (?)

- You should first clone and install the `sntcreos/ducpropagationjava` project
    - `mvn clean install` from `sntcreos/ducpropagationjava` cloned folder
- Generate the jar file of the benchmark project:
    - `mvn clean install` from the root folder of this project

# Run benchmarks:

Before running them, better to check that they can be executed without any error. To perform this check, one can execute the following command: `java -jar <PATH>/approximator-bench.jar duc.propagation.bench -foe true -bm ss -f 0 -i 1 -t max -w 0 -wi 0`

- `duc.propagation.bench`: runs all benchmarks that are in the `duc.propagation.bench` Java package
- `-foe true`: fails if any error is encountered during the execution
- `-bm ss`: sets benchmark to SingleShotTime mode (run once)
- `-f 0`: disables forking mechanism
- `-i 1`: executed only 1 measurement iteration
- `-t max`: uses as many threads as possible (limited by the hardware of the machine)
- `-w 0`: sets to 0s the time to spend warming up
- `-wi 0`: sets the number of warm-up iterations to 0 (redundant with previous options?)



Suggested command line: `java -jar <PATH>/approximator-bench.jar duc.propagation.bench -foe true -gc true -rff results.csv -t max`

- `duc.propagation.bench`: runs all benchmarks that are in the `duc.propagation.bench` Java package
- `-foe true`: fails if any error is encountered during the execution
- `-gc true`: forces GC between each execution
- `-rff results.csv`: exports results in the `results.csv` CSV file
- `-t max`: uses as many threads as possible (limited by the hardware of the machine)

# Benchmarks

## duc.propagation.ignoredbench.*

Old or benchmarks used for manual testing. As suggested by their names, they can be ignored.

## duc.propagation.bench.*

Here to quantify the loss in performances of uncertain load approximation.

### 2020.04.0D-SNAPSHOT

- Benchmarks are executed over 3 simple topologies, that come from the different scenarios: parallel cables at the transformer (ParaTransformer*), parallel cables at a cabinet (ParaCabinet*), and indirect parallel cables (IndirectPara)
- Variables changed among the different executions: the number of uncertain fuses, for 0 to all of them
- All executions will be compared to the "reference" (children of GenReference) benchmark, i.e., execution of the certain load approximation
   