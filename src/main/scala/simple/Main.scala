package simple

import akka.actor.{Props, ActorSystem}
import com.typesafe.config.ConfigFactory

object Main {

  def main(args: Array[String]) {

    if (args.isEmpty) {
      startup(Seq("2551", "2552", "0"))
    } else {
      startup(args)
    }
  }

  def startup(ports: Seq[String]) {
    ports.foreach { port =>
      val config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).withFallback(ConfigFactory.load())
      val system = ActorSystem("ClusterSystem", config)
      val actor = system.actorOf(Props(new SimpleClusterListener), name="clusterListener")
      actor ! "test"

    }
  }
}
