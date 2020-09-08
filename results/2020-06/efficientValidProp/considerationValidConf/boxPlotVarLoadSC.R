xpData = read.csv(file= "results-varload-sc.csv", header=TRUE)

xpData$newCableId = xpData$cableId
indexes = xpData$newCableId < 10
xpData$newCableId[indexes] = paste("0", xpData$newCableId[indexes], sep="")

xpData$combined = paste(xpData$newCableId, xpData$categorie, sep="")

xVal = sort(unique(xpData$combined))
idxNaives = grepl("naive", xVal, TRUE)
colorsBoxes = ifelse(idxNaives, "gray50", "gray80")

par(mgp=c(2.5,1,0), mar=c(4,4,2,0))
boxplot(xpData$stdLoad~xpData$combined,
        col=colorsBoxes,
        ylab = "Standard deviation of cable load",
        xlab = "Cable",
        xaxt='n',
        # names = c("1", "", "2", "", "3", "", "4", "", "5", "", "6", "", "7", "", "8", "", "9", "", "10", "", "11", "", "12", "", "13", "", "14", "", "15", ""),
        # las = 2,
        cex.lab=1.5,
        cex.axis=1.5
        )

legend("topleft", 
       legend = c("Only valid configurations","All configurations") , 
       col = colorsBoxes , 
       bty = "n", 
       pch=20, 
       pt.cex = 3, 
       cex = 1.5, 
       horiz = TRUE, 
       inset = c(0, -0.14),
       xpd = TRUE,
)

abline(v=seq(from=2.5, to=28.5, by=2), col="grey", lty=2)

# mtext(
#      text = "        1     2      3     4      5      6     7      8     9    10    11    12   13    14    15",
#      side= 1,
#      adj=0
#      )

mtext(text=1:9, side = 1, adj = 0, at= c(seq(from=1.2, by=2, length.out = 9)), cex = 1.5, line = 0.5)
mtext(text=10:15, side = 1, adj = 0, at= c(seq(from=18.6, by=2.05, length.out = 6)), cex = 1.5, line = 0.5)