import io.github.nwtgck.ticktacktoe._
import io.github.nwtgck.ticktacktoe.player.{HumanPlayer, MinimaxPlayer, Player}

import scala.util.{Random, Try}


/**
  * Created by Jimmy on 29/5/17.
  */
object Main {

  def main(args: Array[String]): Unit = {

    // Get random seed
    val randomSeed: Int = inputRandomSeed()
    val random = new scala.util.Random(seed = randomSeed)


    var table = Table.initial()
    var turn: Cell = Circle


    val player1 = decidePlayer(Circle, random)
    val player2 = decidePlayer(Cross, random)


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
    println(table)
    table.winnerOpt() match {
      case Some(winner) =>
        println(s"Winner: ${winner}")
      case None =>
        println("Result: draw")
    }

  }


  /**
    * Get player decision
    * @return
    */
  def decidePlayer(turn: Cell /** != Empty **/, random: Random): Player = {
    var playerTry: Try[Player] = null
    do{
      println(s"${turn} (human:1, minimax:2):")
      playerTry = Try{
        val i = scala.io.StdIn.readLine().toInt
        require(i == 1 || i == 2)
        i match {
          case 1 => new HumanPlayer()
          case 2 => new MinimaxPlayer(random, turn)
        }
      }
    } while(playerTry.isFailure)

    playerTry.get
  }


  /**
    * Input random seed
    * @return
    */
  def inputRandomSeed(): Int = {
    var seedTry: Try[Int] = null
    do {
      print("Random seed: ")
      seedTry = Try(scala.io.StdIn.readLine().toInt)
    } while(seedTry.isFailure)

    seedTry.get
  }

}
