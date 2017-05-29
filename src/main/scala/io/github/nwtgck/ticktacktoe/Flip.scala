package io.github.nwtgck.ticktacktoe

object Flip{
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
}
