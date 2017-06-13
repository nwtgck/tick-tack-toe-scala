package io.github.nwtgck.ticktacktoe

import io.github.nwtgck.ticktacktoe.player.Player

/**
  * Created by Ryo on 2017/06/13.
  */
object GameOrganizer {

  /**
    *
    * @param nPlay the number of plays
    */
  def runGame(player1: Player, player2: Player, showTable: Boolean = true, nPlay: Int = 1): Unit = {

    var table = Table.initial()
    var turn: Cell = Circle

    while (!table.isFilled && table.winnerOpt().isEmpty) {
      println(table)

      val pos: (Int, Int) =
        if(turn == Circle){
          player1.move(table)
        } else {
          player2.move(table)
        }

      table = table.updated(pos, turn)
      turn = FlipCell.flipCell(turn)
    }

    if(showTable)
      // Display table
      println(table)

    table.winnerOpt() match {
      case Some(winner) =>
        println(s"Winner: ${winner}")
      case None =>
        println("Result: draw")
    }
  }
}
