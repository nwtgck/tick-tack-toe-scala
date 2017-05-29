package io.github.nwtgck.ticktacktoe

object Minimax{
  /**
    * Find the best for turn
    * @param turn
    * @return
    */
  def bestPos(table: Table, turn: Cell, random: scala.util.Random): (Int, Int) = {
    require(turn != Empty)
    val candidatePoss: Seq[(Int, Int)] = for{
      i <- 0 to 2
      j <- 0 to 2
      if table((i, j)) == Empty
    } yield (i, j)


    random.shuffle(candidatePoss).maxBy{pos =>
      minimax(table, turn, turn, pos)
    }
  }

  def minimax(table: Table, fo: Cell, turn: Cell, pos: (Int, Int)): Int = {

    val updatedTable = table.updated(pos, turn)


    if(updatedTable.isFilled || updatedTable.winnerOpt().isDefined){

      updatedTable.winnerOpt() match {
        // Win or Lose
        case Some(winner) => if(winner == fo) 1 else -1
        // Draw
        case None         => 0
      }
    } else {
      val evaluatedValues: Seq[Int] = for{
        i <- 0 to 2
        j <- 0 to 2
        if updatedTable((i, j)) == Empty
      } yield {
        val nextTurn   = FlipCell.flipCell(turn)
        minimax(updatedTable, fo, nextTurn, (i, j))
      }

      if(fo == turn){
        evaluatedValues.min
      } else {
        evaluatedValues.max
      }
    }
  }
}

