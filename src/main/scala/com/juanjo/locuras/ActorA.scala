package com.juanjo.locuras

import akka.actor.{Actor, Props}
import akka.contrib.pattern.Aggregator
import org.apache.spark.sql.Row
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import scala.concurrent.duration._

object ActorA {
  def props(): Props = Props(new ActorA())
}

class ActorA extends Actor with Aggregator{
  import context.dispatcher
  val propsSeq = List(ActorB.props(), ActorB.props())
  val actors =propsSeq.map(context.actorOf(_))
//  val actorB = context.actorOf(ActorB.props())

//  import scala.concurrent.ExecutionContext.Implicits.global

  implicit val timeout = Timeout(10 second)
  expectOnce {
    case row:Row =>
      val newFuture = actors.head ? row
      newFuture pipeTo sender
  }
}

case class Toma(row:Row)
