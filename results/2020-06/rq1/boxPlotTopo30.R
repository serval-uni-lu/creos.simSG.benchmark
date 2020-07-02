xpData = read.csv("result-bp-topo30.csv", header = TRUE)

par(mar=c(4,4,0,0))

boxplot(xpData$X.V.TV ~ xpData$X.UF,
        data=xpData,
        ylab="Ratio of valid configurations",
        xlab="Number of uncertain fuses (over 30)"
)