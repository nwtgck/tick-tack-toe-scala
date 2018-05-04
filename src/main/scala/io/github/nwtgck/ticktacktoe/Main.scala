import io.github.nwtgck.ticktacktoe._
import io.github.nwtgck.ticktacktoe.player.{HumanPlayer, MinimaxPlayer, Player, QLearingPlayer}

import scala.util.{Random, Try}


/**
  * Created by Jimmy on 29/5/17.
  */
object Main {

  def main(args: Array[String]): Unit = {

    // Get random seed
    val randomSeed: Int = inputRandomSeed()
    val random = new scala.util.Random(seed = randomSeed)



    val p1 = new QLearingPlayer(random = random)
    val p2 = new MinimaxPlayer(random, Cross)//new QLearingPlayer(random = random)


    QLearning.learn(random, p2, showTable = false, nPlay = 100000)

    p1.epsilon = 0

    GameOrganizer.runGame(p1, new HumanPlayer(), showTable = true, nPlay = 100)

//    println(p1.qTable)
//    println(p2.qTable)

//    // Decide players by user input
//    val player1 = decidePlayer(Circle, random)
//    val player2 = decidePlayer(Cross, random)
//
//    // Start game
//    GameOrganizer.runGame(player1, player2, showTable = true, nPlay = 1)
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
