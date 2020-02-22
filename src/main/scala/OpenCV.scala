import java.nio.file.{Files, Paths, StandardCopyOption}
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions.udf
import org.opencv.core.Core.{NATIVE_LIBRARY_NAME, mean}
import org.opencv.core.{CvType, Mat}
import org.opencv.imgproc.Imgproc.{COLOR_BGR2HSV, COLOR_GRAY2BGR, cvtColor}

object OpenCV {
  System.loadLibrary(NATIVE_LIBRARY_NAME) // load OpenCV
  val dropLeft = 7 // drop file://
  val config: Config = ConfigFactory.load() // load resources/reference.conf
  val input: String = config.getString("conf.in")
  val output: String = config.getString("conf.out")

  def findV(height: Int, width: Int, channel: Int, data: Array[Byte]): Int = {
    val image = new Mat(height, width, CvType.CV_8UC(channel))
    image.put(0, 0, data)

    if (image.channels() == 1)
      cvtColor(image, image, COLOR_GRAY2BGR, 3)

    cvtColor(image, image, COLOR_BGR2HSV, 3)
    val averageV = mean(image).`val`(2)
    val normalizedV = (1 - averageV / 255) * 100
    normalizedV.toInt
  }

  def findV_UDF: UserDefinedFunction = udf[Int, Int, Int, Int, Array[Byte]](findV)

  def fixPath: String => String = (x: String) => x.substring(dropLeft, x.length)

  def fixPath_UDF: UserDefinedFunction = udf[String, String](fixPath)

  def main(args: Array[String]): Unit = {

    def classifier(path: String, value: Int): String = {
      val label = if (value >= args(0).toInt) "dark" else "bright"
      val extension = path.split("\\.")
      val name = extension(0).split("/").last
      output + "/" + name + "_" + value + "_" + label + "." + extension(1)
    }

    def classifier_UDF: UserDefinedFunction = udf[String, String, Int](classifier)

    val spark = SparkSession.builder()
      .master("local")
      .appName("OpenCV")
      .getOrCreate()

    val photos = spark.read.format("image")
      .option("dropInvalid", value = true)
      .load(input).select("image.*")

    val V = photos
      .withColumn("V", findV_UDF(photos("height"), photos("width"), photos("nChannels"), photos("data")))
      .withColumn("fixedPath", fixPath_UDF(photos("origin")))

    val classified = V
      .withColumn("pathClassified", classifier_UDF(V("fixedPath"), V("V")))

    val copyFrom = classified.select("fixedPath").collect.map(_.getString(0)).toList
    val copyTo = classified.select("pathClassified").collect.map(_.getString(0)).toList

    copyFrom.zip(copyTo).map({
      case (x, y) =>
        Files.copy(Paths.get(x), Paths.get(y), StandardCopyOption.REPLACE_EXISTING)
    })

    spark.stop()
  }
}