# Spark Image Classification Project

---
The goal of the project was to classify photos into 2 classes - bight & dark - according to their brightness level in an efficient and scalable way. The application's main code is in the **src/main/scala/OpenCV.scala** file. It is a Spark application written with a DataFrame API in Scala with the use of the **bytedeco OpenCV** library for Java and built on the sbt tool.

After running the program photos with attached metadata (brightness level from 0 to 100 and its class) are copied from the **in** to the **out** directory. There directories can be changed in the **reference.conf** file. Prior installation of the OpenCV for Java may be needed. The file **libopencv_javaXXX.so** is supposed to be put in the java.library.path.

---

Even though it is a classification problem, I have decided not to use any ML algorithm for that. Detecting any patterns in photos was not needed to achieve the goal neither. I have written an algorithm with the OpenCV and managed to get it run in a distributed way on the Spark cluster.

The algorithm transforms photos from the RGB format to the HSV one. The average (V)alue of the photo is an indicator how bright it is. Thanks to this, after the normalization of the value score to the 0-100 range, photos can be correctly classified into 2 classes with almost a 20-score-point margin.

The user needs to choose a classification threshold, however, it should not be problem since most of "dark" pictures are in the area of 93-99.
