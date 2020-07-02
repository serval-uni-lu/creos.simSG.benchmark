xpData = read.csv(file="results-varload-ls.csv", header = TRUE)


par(mar=c(4,4,0,0))
boxplot(
  xpData$std ~ xpData$version,
  xlab = "Approah used",
  names = c("naive", "with business rules"),
  ylab = "Standard deviation of the cable load for all configurations"
        )