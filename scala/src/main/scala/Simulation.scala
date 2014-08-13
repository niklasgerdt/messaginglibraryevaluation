package eu.route20.hft.simulation

class SimulationRunner(simulators: List[Simulator]) {

  def run(): Unit = {
  	simulators.par.foreach((s:Simulator) => s.simulate())
  }
}

trait Simulator {
	def simulate(): Unit
}

class ConfigurableSimulator extends Simulator{
	def simulate(): Unit = {
	}
}