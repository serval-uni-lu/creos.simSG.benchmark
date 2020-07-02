
valDerived <- read.csv(file = "heatMat-listValidDerived.csv", header = TRUE)
valNaive <- read.csv(file = "heatMat-listValidRule1.csv", header = TRUE)

colnames(valDerived) <- paste0("", c("02", "04", "06", "08", "10", "12", "16", "30"))
rownames(valDerived) <- paste0("#", c(paste0("0", 0:9), 10:22))
colnames(valNaive) <- paste0("", c("02", "04", "06", "08", "10", "12", "16", "30"))
rownames(valNaive) <- paste0("#", c(paste0("0", 0:9), 10:22))

diff = valNaive - valDerived

means = c(
  mean(diff$`02`, na.rm = TRUE),
  mean(diff$`04`, na.rm = TRUE),
  mean(diff$`06`, na.rm = TRUE),
  mean(diff$`08`, na.rm = TRUE),
  mean(diff$`10`, na.rm = TRUE),
  mean(diff$`12`, na.rm = TRUE),
  mean(diff$`16`, na.rm = TRUE),
  mean(diff$`30`, na.rm = TRUE)
)

mmean = mean(means)

