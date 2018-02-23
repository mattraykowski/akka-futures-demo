import akka.actor.Actor
import akka.event.Logging
import akka.pattern.{after, pipe}

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._

class SimpleActor extends Actor {
  val log = Logging(context.system, this)
  implicit val ec: ExecutionContext = context.system.dispatcher

  override def receive: Receive = {
    case GetWidget(name) => {
      log.info(s"getting widget ${name}")
      sender ! s"got ${name}"
    }
    case GetFromDB => {
      log.info("getting a thing from the db")
      val delayed = after(1 seconds, using = context.system.scheduler) {
        Future(ObjectFromDB("ben"))
      }
      delayed pipeTo sender
    }
    case GetFailure => {
      // I know that I set my timeout to 5 seconds. This will take too long.
      val delayed = after(6 seconds, using = context.system.scheduler) {
        Future(ObjectFromDB("ben"))
      }
      delayed pipeTo sender
    }
    case _ => log.info("received a message")
  }
}
