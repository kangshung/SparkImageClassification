name := "AreYouAfraidOfTheDark"

version := "0.4"

scalaVersion := "2.11.12"

// https://mvnrepository.com/artifact/org.apache.spark/spark-core
libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.5"

// https://mvnrepository.com/artifact/org.apache.spark/spark-sql
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.5"

// https://mvnrepository.com/artifact/org.apache.spark/spark-mllib
libraryDependencies += "org.apache.spark" %% "spark-mllib" % "2.4.5" % "runtime"

// https://mvnrepository.com/artifact/com.typesafe/config
libraryDependencies += "com.typesafe" % "config" % "1.4.0"

//resolvers += "MMLSpark Repo" at "https://mmlspark.azureedge.net/maven"
//libraryDependencies += "com.microsoft.ml.spark" %% "mmlspark" % "1.0.0-rc1"

// https://mvnrepository.com/artifact/org.bytedeco/opencv
libraryDependencies += "org.bytedeco" % "opencv" % "4.1.2-1.5.2"