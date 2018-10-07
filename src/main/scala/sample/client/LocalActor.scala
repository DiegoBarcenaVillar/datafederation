package sample.client

import java.io.File

import akka.actor.{Props, Actor, ActorSystem}
import com.typesafe.config.ConfigFactory

/**
  * Local actor which listens on any free port
  */

class LocalActor extends Actor{

  @throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    /*
      Connect to remote actor. The following are the different parts of actor path
      akka.tcp : enabled-transports  of remote_application.conf
      RemoteSystem : name of the actor system used to create remote actor
      127.0.0.1:5150 : host and port
      user : The actor is user defined
      remote : name of the actor, passed as parameter to system.actorOf call
     */
    val remoteActor = context.actorSelection("akka.tcp://ClusterSystem@127.0.0.1:4579")
    println("That 's remote:" + remoteActor)
    remoteActor ! "TFM"
  }
  override def receive: Receive = {

    case msg:String => {
      println("got message from remote" + msg)
    }
  }
}



object LocalActor {

  def main(args: Array[String]) {

    val configFile = getClass.getClassLoader.getResource("local_client.conf").getFile
    val config = ConfigFactory.parseFile(new File(configFile))
    val system = ActorSystem("ClientSystem",config)
    val localActor = system.actorOf(Props[LocalActor], name="local")
  }

}