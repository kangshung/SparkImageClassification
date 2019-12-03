//import java.awt.Color
//
//import org.apache.spark.sql.SparkSession
//import org.apache.spark.sql.functions._
//
//
//object Model {
//  def main(args: Array[String]): Unit = {
//    val spark = SparkSession.builder()
//      .master("local")
//      .appName("test")
//      .getOrCreate()
//
//    import spark.implicits._
//
//    val data = spark.read.format("image")
//      .load("/photos/*")
//      .select("image.data", "image.nChannels")
//    //      .withColumn("dataRGB", hexToLong($"data"))
//
//    def hexToLong = udf((hex: String) => java.lang.Long.parseLong(hex.trim(), 16))
//
//    def rgb2hsv(rgb: List[Double]): List[Double] = {
//      Color.RGBtoHSB(rgb(2).toInt, rgb(1).toInt, rgb.head.toInt, null).to[List].map(_.toDouble)
//    }
//
//    spark.stop()
//  }
//}