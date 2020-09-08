# cairo_pdf("ratioValidConf-Topo30.pdf", width = 8, height = 6)

xpData = read.csv("result-bp-topo30.csv", header = TRUE)

par(mgp=c(2.5,1,0), mar=c(4,4,0,0))

boxplot(xpData$X.V.TV ~ xpData$X.UF,
        data=xpData,
        ylab="Ratio of valid configurations",
        xlab="Number of uncertain fuses (over 30)",
        cex.lab=1.5,
        cex.axis=1
)

# dev.off()

