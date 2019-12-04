import java.io.File

import org.opencv.core.Core.{NATIVE_LIBRARY_NAME, mean}
import org.opencv.imgcodecs.Imgcodecs.imread
import org.opencv.imgproc.Imgproc.{cvtColor, COLOR_BGR2HSV}

object OpenCV {
  System.loadLibrary(NATIVE_LIBRARY_NAME)

  def findV(path: String): Double = {
    val picture = imread(path)
    cvtColor(picture, picture, COLOR_BGR2HSV, 3)
    val averageV = mean(picture).`val`(2)
    val normalizedV = (1 - averageV / 255) * 100
    normalizedV
  }

  def main(args: Array[String]): Unit = {
    val input = "/photos/images"

    val sources = new File(input).listFiles.par
    val values = sources.map(x => findV(x.toString).floor)

    println(sources)
    println(values)
    println(values.length)
  }
}