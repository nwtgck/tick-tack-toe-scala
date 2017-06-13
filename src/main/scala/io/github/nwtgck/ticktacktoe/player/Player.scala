package io.github.nwtgck.ticktacktoe.player

import io.github.nwtgck.ticktacktoe.Table

/**
  * Created by Ryo on 2017/06/13.
  */
abstract class Player {
  def move(table: Table): (Int, Int)
}
