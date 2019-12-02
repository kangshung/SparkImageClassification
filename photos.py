import cv2
import glob
import numpy as np
import pandas as pd


def findValue(path):
    BGR = cv2.imread(path)
    HSV = cv2.cvtColor(BGR, cv2.COLOR_BGR2HSV)
    V = HSV[:, :, 2]
    return (1 - (np.sum(V) / V.size / 255)) * 100


pictures = glob.glob("/photos/*/*")

output = pd.DataFrame({"source": pictures, 'value': [findValue(x) for x in pictures]})
print(output)
