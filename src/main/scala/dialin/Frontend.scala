package dialin

import akka.actor._
import com.typesafe.config.ConfigFactory
import akka.actor.Terminated
import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.duration._
import akka.util.Timeout
import scala.util.Success

class Frontend extends Actor {

  var backends = IndexedSeq.empty[ActorRef]
  var counter = 0

  def receive = {

    case job: TransformationJob if backends.isEmpty =>
      sender() ! TransformationFailure("service unavailable, try later", job)

    case job: TransformationJob =>
      backends(counter % backends.size) forward job
      counter += 1

    case BackendRegistration if !backends.contains(sender()) =>
      context watch sender()
      backends = backends :+ sender()

    case Terminated(x) =>
      backends = backends.filterNot(_ == x)
  }
}


object Frontend {
  def main(args: Array[String]): Unit = {

    val port = if (args.isEmpty) "0" else args(0)

    val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port").
      withFallback(ConfigFactory.parseString(s"akka.cluster.roles = [frontend]")).
      withFallback(ConfigFactory.load())

    val system = ActorSystem("ClusterSystem", config)

    val frontend = system.actorOf(Props(new Frontend), name="frontend")

    import system.dispatcher
    val c = new AtomicInteger
    implicit val timeout = Timeout(5 seconds)
    import akka.pattern.ask

    system.scheduler.schedule(2 seconds, 2 seconds) {

      (frontend ? TransformationJob("hello-" + c.incrementAndGet())) onComplete {
        case x =>
          println(x)
      }
    }


  }
}