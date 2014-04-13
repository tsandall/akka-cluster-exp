package dialin

import akka.actor.{Props, ActorSystem, RootActorPath, Actor}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.{UnreachableMember, InitialStateAsEvents, MemberUp}
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory
import com.typesafe.scalalogging.slf4j.Logger

class Backend extends Actor {

  val cluster = Cluster(context.system)

  override def preStart() {
    cluster.subscribe(self, initialStateMode = InitialStateAsEvents, classOf[MemberUp], classOf[UnreachableMember])
  }

  override def postStop() {
    cluster.unsubscribe(self)
  }

  val logger = Logger(LoggerFactory.getLogger("Backend"))

  def receive = {

    case MemberUp(m) =>
      if (m.hasRole("frontend")) {
        context.actorSelection(RootActorPath(m.address) / "user" / "frontend") ! BackendRegistration
      }

    case msg @ TransformationJob(text) =>
      logger.info(s"Received $msg, sleeping for 1 second...")
      Thread.sleep(1000)
      sender() ! TransformationResult(text.toUpperCase)

  }


}

object Backend {
  def main(args: Array[String]) {

    val port = if (args.isEmpty) "0" else args(0)

    val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port").
      withFallback(ConfigFactory.parseString("akka.cluster.roles = [backend]")).
      withFallback(ConfigFactory.load())

    val system = ActorSystem("ClusterSystem", config)

    system.logConfiguration()

    system.actorOf(Props(new Backend), name="backend")
  }

}
