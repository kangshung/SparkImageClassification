from pyspark.ml.image import ImageSchema
from pyspark.sql import SparkSession
from pyspark.sql.functions import col

spark = SparkSession.builder \
    .master("local") \
    .appName("test") \
    .getOrCreate()

images = spark.read.format("image").load("/photos/bright")
images.select("image.data").withColumn("tmp", col("data").cast("integer"))

ImageSchema.readImages("/photos/bright").show(20, 100)
spark.stop()
