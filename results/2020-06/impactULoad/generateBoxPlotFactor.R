xpData = read.csv(file = "sgc-2020-impactuload-factor-sc.csv", header = TRUE)

par(mar=c(4, 4, 0, 0))

boxplot(factor~cableId,
        data=xpData,
        ylab="Factor",
        xlab="Cable",
        outline= TRUE
)