import scala.util.Try

sealed trait Cell
case object Empty  extends Cell
case object Circle extends Cell
case object Cross  extends Cell

class Table(private val inner: Array[Array[Cell]]){

  /**
    * Return a cell in the specific position
    * @param pos
    * @return
    */
  def apply(pos: (Int, Int)): Cell = {
    val (i : Int, j : Int) = pos
    require(0 <= i && i <= 2)
    require(0 <= j && j <= 2)
    inner(i)(j)
  }

  /**
    * Update cell in the specific position
    * @param pos
    * @param newCell
    * @return
    */
  def updated(pos: (Int, Int), newCell: Cell): Table = {
    val (i : Int, j : Int) = pos
    require(0 <= i && i <= 2)
    require(0 <= j && j <= 2)
    val newRow = inner(i).updated(j, newCell)
    new Table(inner.updated(i, newRow))
  }

  /**
    * Winner (optional)
    * @return
    */
  def winnerOpt(): Option[Cell /** Restrict: cell != Empty **/] = {
    // Extract each element
    val Array(
      Array(c0, c1, c2),
      Array(c3, c4, c5),
      Array(c6, c7, c8)
    ) = inner

    if(c0 == c1 && c0 == c2 && c0 != Empty){
      Some(c0)
    } else if(c3 == c4 && c3 == c5 && c3 != Empty){
      Some(c3)
    } else if(c6 == c7 && c6 == c8 && c6 != Empty) {
      Some(c6)
    } else if(c0 == c3 && c0 == c6 && c0 != Empty){
      Some(c0)
    } else if(c1 == c4 && c1 == c7 && c1 != Empty){
      Some(c1)
    } else if(c2 == c5 && c2 == c8 && c2 != Empty) {
      Some(c2)
    } else if(c0 == c4 && c0 == c8 && c0 != Empty) {
      Some(c0)
    } else if(c2 == c4 && c2 == c6 && c2 != Empty){
      Some(c2)
    } else {
      None
    }
  }

  /**
    * Check whether this table is filled with non-Empty cell
    * @return
    */
  def isFilled: Boolean = {
    inner.forall(row => !row.contains(Empty))
  }


  /**
    * Table's string expression
    * @return
    */
  override def toString: String = {
    var out: String = ""
    for(row <- inner){
      for(cell <- row){
        val cellString: String = cell match {
          case Empty  => "-"
          case Circle => "O"
          case Cross  => "X"
        }
        out += cellString + " "
      }
      out += "\n"
    }
    out
  }
}

object Table{
  /**
    * Initial table
    * @return
    */
  def initial(): Table = {
    new Table(Array.fill(3)(Array.fill(3)(Empty)))
  }
}

object Minimax{
  /**
    * Find the best for turn
    * @param turn
    * @return
    */
  def bestPos(table: Table, turn: Cell): (Int, Int) = {
    require(turn != Empty)
    val candidatePoss: Seq[(Int, Int)] = for{
      i <- 0 to 2
      j <- 0 to 2
      if table((i, j)) == Empty
    } yield (i, j)

    candidatePoss.maxBy{pos =>
      minimax(table, turn, turn, pos)
    }
  }

  /**
    * Flip the cell
    * @param cell
    * @return
    */
  def flipCell(cell: Cell): Cell = {
    require(cell != Empty)
    cell match {
      case Circle => Cross
      case Cross  => Circle
    }
  }

  def minimax(table: Table, fo: Cell, turn: Cell, pos: (Int, Int)): Int = {

    val updatedTable = table.updated(pos, turn)


    if(updatedTable.isFilled){

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
        val nextTurn   = flipCell(turn)
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

/**
  * Created by Jimmy on 29/5/17.
  */
object Main {

  def main(args: Array[String]): Unit = {

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
            Minimax.bestPos(table, turn)
          }
        case None =>
          Minimax.bestPos(table, turn)
      }

      table = table.updated(pos, turn)
      turn = Minimax.flipCell(turn)
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
      val triedInt: Try[Int] = Try(scala.io.StdIn.readLine().toInt)
      triedPos = Try{
        val ij: Int = triedInt.get
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
