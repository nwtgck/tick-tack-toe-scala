package io.github.nwtgck.ticktacktoe

import io.github.nwtgck.ticktacktoe.player.{Player, QLearingPlayer}

import scala.util.Random

/**
  * Created by Ryo on 2017/06/13.
  */
object QLearning {

  /**
    *
    * @param nPlay the number of plays
    */
  def learn(random: Random, player2: Player, showTable: Boolean = true, nPlay: Int = 100): Unit = {


    val player1 = new QLearingPlayer(random=random)

    for(i <- 1 to nPlay) {
      var table = Table.initial()
      var turn: Cell = Circle
      var gameEnd: Boolean = false

      do {
        if(showTable) {
          println(table)
        }

        val currentPlayer: Player =
          if (turn == Circle) {
            player1
          } else {
            player2
          }

        val pos: (Int, Int) = currentPlayer.move(table)

        table = table.updated(pos, turn)
        turn = FlipCell.flipCell(turn)

        gameEnd = table.isFilled || table.winnerOpt().nonEmpty

        if (gameEnd) {
            if (table.winnerOpt() == Some(Circle))
              player1.updateQ(table, 1.0f)
            else
              player1.updateQ(table, -1.0f)

          player2 match {
            case p: QLearingPlayer =>
              if (table.winnerOpt() == Some(Cross))
                p.updateQ(table, 1.0f)
              else
                p.updateQ(table, -1.0f)
            case _ => {}
          }
        } else {
          currentPlayer match {
            case p: QLearingPlayer =>
              p.updateQ(table, 0.0f)
            case _ => {}
          }
        }

      } while (!gameEnd)

      if (showTable) {
        // Display table
        println(table)
      }

      table.winnerOpt() match {
        case Some(winner) =>
          println(s"(${i}) Winner: ${winner}")
        case None =>
          println(s"(${i}) esult: draw")
      }
    }
  }
}
