package eu.route20.hft.app

trait KillSig {
  def kill: () => Boolean
}

trait NeverKill extends KillSig {
  override def kill =  () => false
}