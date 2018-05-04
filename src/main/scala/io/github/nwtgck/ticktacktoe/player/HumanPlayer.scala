package io.github.nwtgck.ticktacktoe.player
import io.github.nwtgck.ticktacktoe.{Empty, Table}

import scala.util.Try

/**
  * Human Player
  */
class HumanPlayer extends Player{
  override def move(table: Table): (Int, Int) = {
    var i: Int = 0
    var j: Int = 0

    var triedPos: Try[(Int, Int)] = null

    do{
      println("""||00|01|02|
                 ||10|11|12|
                 ||20|21|22|""".stripMargin)
      print("Your pos: ")
      triedPos = Try{
        val ij: Int = scala.io.StdIn.readLine().toInt
        val i: Int = ij / 10
        val j: Int = ij % 10
        require(0 <= i && i <= 2)
        require(0 <= j && j <= 2)
        require(table((i, j)) == Empty)
        (i, j)
      }
    } while (triedPos.isFailure)


    triedPos.get
  }
}
