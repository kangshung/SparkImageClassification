# Are you afraid of the dark?
##### Damian Fiłonowicz

---

The application consists of 1 Scala object called *OpenCV* in the OpenCV.scala file. It is built on the sbt tool, Java 1.8.0 openjdk, Scala 2.11.12.

After cloning, ```sbt "run 75" ``` command runs the program => photos with attached metadata are copied from the **in** to the **out** directory.

In and out directories can be changed in the **reference.conf** file. Prior installation of the OpenCV for Java may be needed. File **libopencv_java412.so** to be put in the java.library.path is in the repository.

---

At the beginning, I tried Scala and Python versions of Apache Spark DataFrame API to obtain a distributed application but as I figured out that loading
photos into Spark is a nightmare and not fully supported I have decided not to follow this idea and focus on other ways of solving the problem.

Even though I have known since the beginning that the task is a classification problem, I have not used any ML algorithm for that. Detecting any patterns
on photos was not needed to achieve the goal neither. I have written an algorithm with the
[OpenCV library for Java](https://opencv-java-tutorials.readthedocs.io/en/latest/01-installing-opencv-for-java.html#install-opencv-3-x-under-linux),
official library [here](https://opencv.org/).
I used functions for reading pictures as an RGB [Mat](https://docs.opencv.org/master/d3/d63/classcv_1_1Mat.html) or Java version
[here](https://docs.opencv.org/master/javadoc/org/opencv/core/Mat.html). Then, converted them to the HSV format.
[HSV](https://en.wikipedia.org/wiki/HSL_and_HSV) (or HSL) is an alternative that can be calculated from RGB and points out the **H**ue,
**S**aturation, and **V**alue (lightness) of the pixel. The average value of the photo is an indicator how bright it is.

Thanks to this, after normalization of the value score to the 0-100 range, photos can be correctly classified into 2 classes as in the task. Demo images can be split
with almost 20-score-point margin. It means that any value between **64** and **83** can positively differentiate **every** bright picture from the "too dark" ones.
However, picking a higher threshold could not be problem since most of "too dark" pictures are in the area of 93-99 score. It means that the application should
work for any other pictures.

The application uses parallel **ParArray** collections that boost its speed. It can be further expanded via real thread-level parallelism or by doing
calculations at least partially on a distributed framework like Spark, or with Akka.
