xpData = read.csv(file="sgc-2020-impactuload-sc-ls.csv", header = TRUE)

par(mar=c(4, 4, 0, 0))

boxplot(std~category, 
        data=xpData, 
        ylab = "Standard deviation of cable loads", 
        xlab="Localisation of cable", 
        names=c("Circle", "Mandatory power flow")
)