package eu.route20.hft.app

import eu.route20.hft.common.Notification

trait KillSig {
  def kill: Notification => Boolean
}

trait NeverKill extends KillSig {
  override def kill = (n: Notification) => true
}