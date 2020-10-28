# install.packages("ggplot2") 
# library("ggplot2")
# install.packages("reshape")
# library("reshape")

perfbsRules <- read.csv(file = "perf-uload-approx-bsrule.csv", header = TRUE)
perfNaive <- read.csv(file = "perf-uload-approx-naive.csv", header = TRUE)

dfToPlot <- data.frame(0:22, perfbsRules$X16, perfNaive$X16)
colnames(dfToPlot) <- c("x", "with rules", "naive")

melted <- melt(dfToPlot, id=c("x"))
melted <- na.omit(melted)

# ggplot(data=dfToPlot, aes(x=x)) +
#   geom_line(aes(y = yBs), linetype="dashed") +
#   geom_point(aes(y = yBs)) +
#   geom_line(aes(y = yNaive)) +
#   geom_point(aes(y = yNaive)) +
#   scale_y_continuous(trans = "log10") +
#   labs(y="Execution time (ms)", x="# Uncertain fuses (over 30)")

# par(mgp=c(4,1,0), mar=c(4,4,0,0))


ggp <- ggplot(data = melted) +
  geom_line(aes(x=x, y=value, linetype=variable)) +
  geom_point(aes(x=x, y=value, shape=variable)) +
  labs(x="Number of uncertain fuses", y="Execution time (ms)", linetype="Version", shape="Version") +
  scale_y_continuous(trans = "log10") +
  theme(legend.position = c(0.3, 0.9), legend.direction = "horizontal",
        axis.text = element_text(size = 12), axis.title = element_text(size = 15),
        legend.text = element_text(size = 15)
        )

ggp
