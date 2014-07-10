package route20.hft.strategy

import route20.hft.Tweet
import akka.actor.Actor

class ExampleStrategy extends Actor {
  
  def receive = {
    case Tweet => println("Hello world!")
  }
}
