package com.juanjo.locuras

import akka.actor.{Actor, Props}
import akka.contrib.pattern.Aggregator
import org.apache.spark.sql.Row
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import scala.concurrent._

object ActorB {
  def props(): Props = Props(new ActorB())
}

class ActorB extends Actor with Aggregator{
  expectOnce {
    case row:Row =>
      sender ! Toma(row)
  }
}


