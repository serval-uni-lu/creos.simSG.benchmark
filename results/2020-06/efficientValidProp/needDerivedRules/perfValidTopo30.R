# install.packages("ggplot2") 
# library("ggplot2")
# install.packages("reshape")
# library("reshape")

perfListDeived <- read.csv(file = "heatMat-listValidDerived.csv", header = TRUE)
perfListGeneral = read.csv(file= "heatMat-listValidRule1.csv", header = TRUE)

toPlot = data.frame(0:22, perfListDeived$X30, perfListGeneral$X30)
colnames(toPlot) = c("x", "derived rules", "general rule")

melted = melt(toPlot, id=c("x"))

ggp = ggplot(data=melted) + 
  geom_line(aes(x=x, y=value, linetype=variable)) + 
  geom_point(aes(x=x, y=value, shape=variable)) + 
  scale_y_continuous(trans = "log10") + 
  labs(x="Number of uncertain fuses", y="Execution time (ms) (log)", linetype="Executed rule(s)", shape="Executed rule(s)") + 
  theme(legend.position = c(0.3, 0.9), legend.direction = "horizontal")

ggp
