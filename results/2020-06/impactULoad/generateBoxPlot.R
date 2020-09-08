xpData = read.csv(file = "sgc-2020-impactuload-sc.csv", header = TRUE)

par(mgp=c(2.5,1,0), mar=c(4,4,0,0))

boxplot(std~cableId,
        data=xpData,
        ylab="Standard deviation of cable loads",
        xlab="Cable",
        cex.lab=1.5,
        cex.axis=1.3
)