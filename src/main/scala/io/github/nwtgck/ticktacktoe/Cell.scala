package io.github.nwtgck.ticktacktoe


sealed trait Cell
case object Empty  extends Cell
case object Circle extends Cell
case object Cross  extends Cell
