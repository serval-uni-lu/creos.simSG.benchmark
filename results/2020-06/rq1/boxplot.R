xpData = read.csv("result-bp.csv", header = TRUE)
xdDF = data.frame(xpData)

boxplot(X.V.TV~X.UF.TF,
        data=xdDF,
        ylab="Ratio of valid configurations",
        xlab="Ratio of uncertain fuses (grouped every 10%)"
)