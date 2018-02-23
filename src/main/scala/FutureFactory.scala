import akka.actor.Scheduler

import scala.concurrent.{ExecutionContext, Future}
import akka.pattern.after

import scala.concurrent.duration.FiniteDuration

object FutureFactory {
  def getLater(delay: FiniteDuration, value: String)(implicit scheduler: Scheduler, ec: ExecutionContext): Future[String] = {
    after(delay, scheduler) {
      Future(value)
    }
  }

}
