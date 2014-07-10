package route20.hft.feed

import akka.actor.Actor
import route20.hft.Tweet

class Twitter extends Actor {
  
  def receive = {
    case Tweet => println("Hello world!")
  }
}