package com.juanjo.locuras

import akka.actor.{Actor, Props}
import akka.contrib.pattern.Aggregator
import org.apache.spark.sql.Row

object ActorA {
  def props(): Props = Props(new ActorA())
}

class ActorA extends Actor with Aggregator{
  expectOnce {
    case row:Row =>
      sender ! Toma(row)
  }


}

case class Toma(row:Row)
