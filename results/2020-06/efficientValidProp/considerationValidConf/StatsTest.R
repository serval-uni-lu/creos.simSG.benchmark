xpData = read.csv(file= "results-varload-sc.csv", header=TRUE)

xpData$newCableId = xpData$cableId
indexes = xpData$newCableId < 10
xpData$newCableId[indexes] = paste("0", xpData$newCableId[indexes], sep="")
xpData$combined = paste(xpData$newCableId, xpData$categorie, sep="")



idxCbl2Naive = xpData$combined == "10naive"
stdCb2Naive = xpData$stdLoad[idxCbl2Naive]

idxCbl2Rule = xpData$combined == "10bs"
stdCb2Rule = xpData$stdLoad[idxCbl2Rule]
wTestCb2 = wilcox.test(stdCb2Naive, stdCb2Rule)
print(wTestCb2)