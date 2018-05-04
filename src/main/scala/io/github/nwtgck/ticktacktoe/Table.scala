package io.github.nwtgck.ticktacktoe


object Table{
  /**
    * Initial table
    * @return
    */
  def initial(): Table = {
    new Table(Array.fill(3)(Array.fill(3)(Empty)))
  }
}

case class Table(private val inner: Array[Array[Cell]]){

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
    * Return empty positoins
    */
  lazy val emptyPoss: Seq[(Int, Int)] = for{
    i <- 0 to 2
    j <- 0 to 2
    if this.apply((i, j)) == Empty
  } yield (i, j)

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


