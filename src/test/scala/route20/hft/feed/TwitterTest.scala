package route20.hft.feed

import akka.testkit.TestActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import route20.hft.Tweet

class TwitterTest{
//	val ref = TestActorRef(new Twitter)
//	val actor = ref.underlyingActor

val twitter = new Twitter

  def run = {
  //	actor ! Tweet("", "", "")
  	twitter receive Tweet("", "", "")
  	  }
}


object Main {
  def main(args: Array[String]): Unit = {
  	val test = new TwitterTest
  	test run
  }
}