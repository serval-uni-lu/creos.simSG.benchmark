xpData = read.csv(file = "sgc-2020-impactuload-sc.csv", header = TRUE)

par(mar=c(4, 4, 0, 0))

boxplot(std~cableId,
        data=xpData,
        ylab="Standard deviation of cable loads",
        xlab="Cable"
)