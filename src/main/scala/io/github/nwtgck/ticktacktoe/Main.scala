import io.github.nwtgck.ticktacktoe._

import scala.util.Try


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
    /** Restrict: cell != turn **/
    val humanOpt: Option[Cell] = humanCellOpt() /** Restrict: cell != turn **/


    while (!table.isFilled && table.winnerOpt().isEmpty) {
      println(table)


      val pos: (Int, Int) = humanOpt match {
        case Some(human) =>
          if (turn == human) {
            playerPos(table)
          } else {
            Minimax.bestPos(table, turn, random)
          }
        case None =>
          Minimax.bestPos(table, turn, random)
      }

      table = table.updated(pos, turn)
      turn = Flip.flipCell(turn)
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
    * Get human cell (optional)
    * if None, Machine vs Machine
    * @return
    */
  def humanCellOpt(): Option[Cell] /** cell != Empty **/ = {
    var cellTry: Try[Option[Cell]] = null
    do{
      println("0: Circle, 1: Cross, 2: Machine vs Machine")
      cellTry = Try{
        val i = scala.io.StdIn.readLine().toInt
        require(i == 0 || i == 1 || i == 2)
        i match {
          case 0 => Some(Circle)
          case 1 => Some(Cross)
          case 2 => None
        }
      }
    } while(cellTry.isFailure)

    cellTry.get
  }


  /**
    * Get player position
    * @param table
    * @return
    */
  def playerPos(table: Table): (Int, Int) = {
    var i: Int = 0
    var j: Int = 0

    var triedPos: Try[(Int, Int)] = null

    do{
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
