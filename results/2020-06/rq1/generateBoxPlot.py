"""
This script parse the result file and create another one,  that will be used to generate the box plot using R
"""

import os
import pandas as pd

DATA_FILE_PATH = os.path.join(".", "result-raw.csv")
DATA_FILE_OUT_PATH = os.path.join(".", "result-bp.csv")
toolbar_width = 40


def get_scale(ratio):
    if ratio <= 0.1:
        return 0.1
    elif ratio <= 0.2:
        return 0.2
    elif ratio <= 0.3:
        return 0.3
    elif ratio <= 0.4:
        return 0.4
    elif ratio <= 0.5:
        return 0.5
    elif ratio <= 0.6:
        return 0.6
    elif ratio <= 0.7:
        return 0.7
    elif ratio <= 0.8:
        return 0.8
    elif ratio <= 0.9:
        return 0.9
    return 1


if __name__ == '__main__':
    if not os.path.exists(DATA_FILE_PATH):
        print("File " + str(DATA_FILE_PATH) + " does not exist.")
        exit(2)

    data = pd.read_csv(DATA_FILE_PATH)
    out = open(DATA_FILE_OUT_PATH, "w")
    out.write("#UF/TF, #V/TV\n")

    del data['File Name']
    del data['Idx Repetition']

    total = len(data)
    prevPerc = -1

    for idx, row in data.iterrows():
        perc = int(round(idx/total * 100))
        if perc > prevPerc:
            print(str(perc) + "%")
            prevPerc = perc

        ratioUFuseFuse = get_scale(row["Nb. UFuses"] / row["Nb. Fuses"])
        ratioValid = row["Nb. Valid Conf."] / row["Nb. Configurations"]

        out.write(str(ratioUFuseFuse) + "," + str(ratioValid) + "\n")

    out.close()

