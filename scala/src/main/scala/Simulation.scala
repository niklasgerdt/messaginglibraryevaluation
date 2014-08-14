package eu.route20.hft.simulation

import eu.route20.hft.publish._
import eu.route20.hft.notification._
import grizzled.slf4j.Logging


class SimulationRunner(simulators: List[Simulator]) {

  def run(): Unit = {
  	simulators.par.foreach((s:Simulator) => s.simulate())
  }
}

trait Simulator {
	def simulate(): Unit
}

case class SimulatorConfig(notifications: Int, notificationLength: Int)

class ConfigurableSimulator(publisher: Publisher, config: SimulatorConfig) extends Simulator with Logging{
	
	def simulate(): Unit = {
		info("simulating "+config.notifications+" notifications")
		var x = 0
		for(x <- 1 to config.notifications) {
			publisher publish(Notification())
		}
	}
}