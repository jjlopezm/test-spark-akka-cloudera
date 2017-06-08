name := "withoutContrib"

version := "2.0"

scalaVersion := "2.10.5"


val scalaVersionBuild = "2.10.5"
val organizationName = "com.juanjo"
val akkaVersion = "2.2.3"
val sparkVersion = "1.6.0"
val sparkCsvVersion = "1.4.0"

lazy val root = (project in file("."))


  .settings(Defaults.itSettings: _*)
  .settings(
    inThisBuild(List(
      organization := organizationName,
      scalaVersion := scalaVersionBuild,
      version := "0.2.0-SNAPSHOT"
    )),
    name := "spark-akka-etl",
    mainClass in assembly := Some("com.juanjo.locuras.Test.main"),
    resolvers ++= Seq(
      "Cloudera CDH 5.0" at "https://repository.cloudera.com/artifactory/cloudera-repos"),
    libraryDependencies ++= Seq(
      "org.apache.spark" % "spark-core_2.10" % "1.6.0-cdh5.7.1" % "provided",
      "org.apache.spark" % "spark-sql_2.10" % "1.6.0-cdh5.7.1" % "provided",
    "com.databricks" % "spark-csv_2.10" % sparkCsvVersion,
      "com.typesafe.akka" %% "akka-contrib" % akkaVersion
        exclude("com.typesafe.akka", "akka-remote_2.10")
        exclude("com.typesafe.akka", "akka-actor_2.10")
        exclude("com.typesafe.akka", "akka-cluster_2.10")

  ))

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)