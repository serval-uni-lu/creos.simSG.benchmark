Data Uncertainty Propagation in smart grid - Pure Java - Benchmark
---

# How to compile (?)

- You should first clone and install the `sntcreos/ducpropagationjava` project
    - `mvn clean install` from `sntcreos/ducpropagationjava` cloned folder
- Generate the jar file of the benchmark project:
    - `mvn clean install` from the root folder of this project

# Run benchmarks:

Before running them, better to check that they can be executed without any error. To perform this check, one can execute the following command: `java -jar target/approximator-bench.jar duc.propagation.bench -foe true -bm ss -f 0 -i 2 -t max -w 0 -wi 0`

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

# Results

## 2020-03-27

- **Command executed**: `java -Xmx16g -jar approximator-bench.jar duc.propagation.bench.RC3 -foe true -gc true -rff results.csv -t max -p nbUncFuses=16`
- **commit id (bench project)**: ???
- **Version DucPropagation-Java**: 2020.04.0D-SNAPSHOT
- **commit id (DucPropagation-Java project)**: ???


## 2020-03-30

- **Command executed**: `java -Xmx16g -jar approximator-bench.jar duc.propagation.bench -foe true -gc true -rff results.csv -t max -wf 3 -f 5`
- **commit id (bench project)**: ???
- **Version DucPropagation-Java**: 2020.04.0D-SNAPSHOT
- **commit id (DucPropagation-Java project)**: ???

## 2020-04-04

- **Command executed**: `java -Xmx16g -jar approximator-bench.jar duc.propagation.bench -foe true -gc true -rff results.csv -t max -wf 3 -f 5`
- **commit id (bench project)**: 24acfd61 
- **Version DucPropagation-Java**: 2020.04.0D-SNAPSHOT
- **commit id (DucPropagation-Java project)**: fb41950a 

## 2020-04-06

- **Command executed**: `java -Xmx16g -jar approximator-bench.jar "duc\\.propagation\\.bench\\..*Reference\\.referenceBench" -e duc.propagation.bench.RC3SReference -foe true -gc true -rff results.csv -t max -wf 3 -f 5`
- **commit id (bench project)**: ba79a983 
- **Version DucPropagation-Java**: 2020.04.0D-SNAPSHOT
- **commit id (DucPropagation-Java project)**: fb41950a 

## 2020-06

These results come from several experiments we perform for a paper submission to the [IEEE SmartGridComm 2020](https://sgc2020.ieee-smartgridcomm.org/) conference.
The code executed for these experiments are in packages with a name that contains the `smartgridcomm2020` string.
The results of those that also contains the `old` string do not appear on the submitted version of the paper.

- **command executed**:
    - scripts in `src/scripts/smartgridcomm2020` for the performance benchmarks or the `duc.propagation.ignoredbench.experiments.smartgridcomm2020.VarianceCableAllVsValid` class
    - directly run the main classes in `duc.propagation.ignoredbench.experiments.smartgridcomm2020` Java package
- **Version BencDucPropJava project**: 2020.07.01
- **Version DucPropagation-Java**: 2020.07.01

Results of all the becnhmarks are saved in the `results/2020-06` folder.
For each execution, we kept the raw data (without being processed).
They are either in a folder called `raw` or contain `raw` in their name.
They have been processed mainly by R script but also with a Python script, saved also in the `results/2020-06` folder.

# Change logs

## 2020.04.0D-SNAPSHOT

- Benchmarks are executed over 3 simple topologies, that come from the different scenarios: parallel cables at the transformer (ParaTransformer*), parallel cables at a cabinet (ParaCabinet*), and indirect parallel cables (IndirectPara)
- Variables changed among the different executions: the number of uncertain fuses, for 0 to all of them
- All executions will be compared to the "reference" (children of GenReference) benchmark, i.e., execution of the certain load approximation
   
## 2020.07.01

- Benchmarks for the SmartGirdComm 2020 paper