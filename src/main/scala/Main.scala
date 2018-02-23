import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.io.StdIn
import scala.util.{Failure, Success}

object Main extends App {
  implicit val system = ActorSystem("futures-test")
  implicit val materializer = ActorMaterializer()
  implicit val timeout = Timeout(5 seconds)

  // Create an instance of our simple actor.
  val simpleActor = system.actorOf(Props[SimpleActor], "simpleActor")

  // Get the system dispatcher for an execution context.
  import system.dispatcher

  // Send a 'message' to the actor.
  simpleActor ! "message"

  // Ask the actor for a response and wait for it.
  val response: Future[String] = (simpleActor ? GetWidget("test")).mapTo[String]
  response onComplete {
    case Success(message) => println(s"got the message: ${message}")
    case Failure(t) => println(s"an error occurred: ${t.getMessage()}")
  }

  // Ask for a delayed response.
  val obj1: Future[ObjectFromDB] = (simpleActor ? GetFromDB).mapTo[ObjectFromDB]
  obj1 onComplete {
    case Success(ObjectFromDB(objectFromDB)) => println(objectFromDB)
    case Failure(t) => println(s"Something went wrong: ${t.getMessage}")
  }

  // Ask for a delayed response which we know will timeout.
  val obj2: Future[ObjectFromDB] = (simpleActor ? GetFailure).mapTo[ObjectFromDB]
  obj2 onComplete {
    case Success(ObjectFromDB(objectFromDB)) => println(objectFromDB)
    case Failure(t) => println(s"Something went wrong: ${t.getMessage}")
  }

  println("Press ENTER to terminate.")
  StdIn.readLine()
  system.terminate()
}
