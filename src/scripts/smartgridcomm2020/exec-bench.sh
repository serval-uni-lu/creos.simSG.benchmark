#!/bin/bash


java --enable-preview -Xmx10g -jar approximator-bench.jar "duc.propagation.bench.smartgridcomm2020.*.listValidConfOur" -foe true -gc true -rff results-listValidConfOur.csv -t max

java --enable-preview -Xmx10g -jar approximator-bench.jar "duc.propagation.bench.smartgridcomm2020.*.listValidConfRule1" -foe true -gc true -rff results-listValidConfRule1.csv -t max

java --enable-preview -Xmx10g -jar approximator-bench.jar "duc.propagation.bench.smartgridcomm2020.*.ucLoadApproxImproved" -foe true -gc true -rff results-ucLoadApproxImproved.csv -t max

java --enable-preview -Xmx10g -jar approximator-bench.jar "duc.propagation.bench.smartgridcomm2020.*.ucLoadApproxNaive" -foe true -gc true -rff results-ucLoadApproxNaive.csv -t max

