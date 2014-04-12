package simple

import akka.actor.Actor
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import akka.cluster.ClusterEvent.MemberUp
import akka.cluster.ClusterEvent.UnreachableMember
import com.typesafe.scalalogging.slf4j.Logger
import org.slf4j.LoggerFactory

class SimpleClusterListener extends Actor {

  val log = Logger(LoggerFactory.getLogger("SimpleClusterListener"))

  val cluster = Cluster(context.system)

  override def preStart(): Unit = {
    cluster.subscribe(self, initialStateMode = InitialStateAsEvents, classOf[MemberEvent], classOf[UnreachableMember])
  }

  override def postStop(): Unit = {
    cluster.unsubscribe(self)
  }

  def receive = {

    case MemberUp(member) =>
      log.info(s"Got UP ${member.address}")

    case UnreachableMember(member) =>
      log.info(s"Got UNREACHABLE $member")

    case MemberRemoved(member, previousStatus) =>
      log.info(s"Got REMOVED $member $previousStatus")

    case x =>
      log.info(s"$self Got $x from ${sender()}")

  }

}
