# install.packages("reshape")
# install.packages("ggplot2")
# library("reshape")
# library("ggplot2")

values <- read.csv(file = "heatMat-listValidDerived.csv", header = TRUE)
# values <- read.csv(file = "heatMat-listValidRule1.csv", header = TRUE)


matrixValue <- data.matrix(values)
colnames(matrixValue) <- paste0("#", c("02", "04", "06", "08", "10", "12", "16", "30"))
rownames(matrixValue) <- paste0("#", c(paste0("0", 0:9), 10:22))
# matrixValue <- log10(matrixValue)
matrixValueMel <- melt(matrixValue)
#colnames(matrixValueMel) <- c("Number of uncertain fuses", "Number of fuses", "Execution time -log10- (ms)")


par(mar=c(0,0,0,0))
# heatmap(matrixValue, Rowv = NA, Colv = NA)
ggp <- ggplot(matrixValueMel, aes(X2, X1)) + 
  geom_tile(aes(fill = value)) + 
  scale_fill_gradient(low = "lightgrey", high = "black", na.value="transparent") + 
  theme_bw() + 
  labs(x="Number of fuses", y="Number of uncertain fuses", fill=expression('log'[10]*' execution time (ms)'), parse = TRUE) + 
  theme(legend.position="bottom")
ggp
