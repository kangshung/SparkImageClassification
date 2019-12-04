import java.io.File

import com.typesafe.config._
import org.opencv.core.Core.{NATIVE_LIBRARY_NAME, mean}
import org.opencv.imgcodecs.Imgcodecs.imread
import org.opencv.imgproc.Imgproc.{COLOR_BGR2HSV, cvtColor}

object OpenCV {
  System.loadLibrary(NATIVE_LIBRARY_NAME)

  def findV(path: String): Int = {
    val picture = imread(path)
    cvtColor(picture, picture, COLOR_BGR2HSV, 3)
    val averageV = mean(picture).`val`(2)
    val normalizedV = (1 - averageV / 255) * 100
    normalizedV.toInt
  }

  def main(args: Array[String]): Unit = {

    val conf = ConfigFactory.load()
    val input = conf.getString("conf.in")
    val output = conf.getString("conf.out")

    val sources = new File(input).listFiles.par.map(x => x.toString)

    val z = sources.map(x => {
      val value = findV(x)
      val label = if (value >= args(0).toInt) "dark" else "bright"
      val name = x.split("\\.", 2)
      name(0).concat("_" + label + "_" + value + "." + name(1))
    })


    println(z)
    println(z.length)
  }
}