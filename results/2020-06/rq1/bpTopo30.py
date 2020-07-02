"""
This script parse the result file and create another one,  that will be used to generate the box plot using R
"""

import os
import pandas as pd

DATA_FILE_PATH = os.path.join(".", "result-topo30.csv")
DATA_FILE_OUT_PATH = os.path.join(".", "result-bp-topo30.csv")
toolbar_width = 40


if __name__ == '__main__':
    if not os.path.exists(DATA_FILE_PATH):
        print("File " + str(DATA_FILE_PATH) + " does not exist.")
        exit(2)

    data = pd.read_csv(DATA_FILE_PATH)
    out = open(DATA_FILE_OUT_PATH, "w")
    out.write("#UF, #V/TV\n")

    del data['File Name']
    del data['Idx Repetition']

    total = len(data)
    prevPerc = -1

    for idx, row in data.iterrows():
        perc = int(round(idx/total * 100))
        if perc > prevPerc:
            print(str(perc) + "%")
            prevPerc = perc

        ratioValid = row["Nb. Valid Conf."] / row["Nb. Configurations"]

        out.write(str(row["Nb. UFuses"]) + "," + str(ratioValid) + "\n")

    out.close()

