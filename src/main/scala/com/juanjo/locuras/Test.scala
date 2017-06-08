package com.juanjo.locuras

import java.util.concurrent.CountDownLatch

import akka.actor.ActorSystem
import akka.pattern.ask
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{Row, SQLContext}
import akka.util.Timeout
import scala.concurrent.duration._
import scala.util.{Failure, Success}


object Test {

  def main (args:Array[String]): Unit ={

    val fileHdfs = "hdfs:///tmp/TCDT040.csv"
    val localFile = "src/main/resources/TCDT050.csv"

    val sparkConf = new SparkConf().setAppName("TestODS")
    val sc = new SparkContext(sparkConf)
    val sqlContext = new SQLContext(sc)

    val tablaDF = sqlContext.read.format("com.databricks.spark.csv")
      .option("header", "true")
      .option("inferSchema","false")
      .load(fileHdfs)

    import scala.concurrent.ExecutionContext.Implicits.global

    implicit val timeout = Timeout(10 second)

    val res = tablaDF.mapPartitions { it: Iterator[Row] =>
      val listRows = scala.collection.mutable.ListBuffer[Row]()

      val rows = it map { case row: Row => row } toSeq

      val rowsProcessedLatch = new CountDownLatch(rows.length)
      implicit val system = ActorSystem(s"LocuraActorSystem")
      rows foreach { row =>
        val actorA = system.actorOf(ActorA.props())

        val resultFuture = actorA ? row

        resultFuture onComplete {
          case Success(Toma(row)) =>
            listRows.append(row)
            rowsProcessedLatch.countDown()
          case Failure(_) =>
            rowsProcessedLatch.countDown()

        }
      }
      rowsProcessedLatch.await()
      system.shutdown
      listRows.toIterator
    }
    println("SALIENDO SISTEMA DE ACTORES")

    res foreach( println _)
  }
}
