import java.io.File
import java.nio.file.{Files, Paths, StandardCopyOption}

import com.typesafe.config.ConfigFactory

import org.opencv.core.Core.{NATIVE_LIBRARY_NAME, mean}
import org.opencv.imgcodecs.Imgcodecs.imread
import org.opencv.imgproc.Imgproc.{COLOR_BGR2HSV, cvtColor}

import scala.collection.parallel.mutable.ParArray


object OpenCV {
  System.loadLibrary(NATIVE_LIBRARY_NAME)

  def findV(path: String): Int = {
    val picture = imread(path)
    cvtColor(picture, picture, COLOR_BGR2HSV, 3)
    val averageV = mean(picture).`val`(2)
    val normalizedV = (1 - averageV / 255) * 100
    normalizedV.toInt
  }

  def readPhotoPaths(input: String): ParArray[String] = {
    new File(input).listFiles.par.map(x => x.toString)
  }

  def createNewPaths(sources: ParArray[String], output: String, threshold: Array[String]): ParArray[String] = {
    sources.map(
      x => {
        val value = findV(x)
        val label = if (value >= threshold(0).toInt) "dark" else "bright"
        val extension = x.split("\\.")
        val filename = extension(0).split("/")
        val outFolder = if (!output.endsWith("/")) output + "/" else output
        outFolder + filename.last + "_" + label + "_" + value + "." + extension(1)
      })
  }

  def main(args: Array[String]): Unit = {
    val projectPath = System.getProperty("user.dir")
    val config = ConfigFactory.load()

    val input = projectPath + config.getString("conf.in")
    val output = projectPath + config.getString("conf.out")

    val sources = readPhotoPaths(input)
    val taggedSources = createNewPaths(sources, output, args)

    //Copies images from the 'in' to the 'out' folder (specified in the reference.conf)
    sources.zip(taggedSources).map({
      case (x, y) =>
        Files.copy(Paths.get(x), Paths.get(y), StandardCopyOption.REPLACE_EXISTING)
    })
  }
}