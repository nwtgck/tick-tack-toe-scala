package io.github.nwtgck.ticktacktoe.player
import io.github.nwtgck.ticktacktoe.Table

import collection.mutable
import scala.util.Random

//object QLearning{
//  type State  = Table
//  type Action = (Int, Int)
//
//  def learnAndCreateQLearningPlayer(epsilon: Float = 0.3f, alpha: Float = 0.9f, gamma: Float = 0.9f, random: Random): QLearingPlayer = {
//    // Q-Table is initialized by random float values
//    val qTable: mutable.Map[State, mutable.Map[Action, Float]] = new mutable.HashMap()().withDefault(_ => new mutable.HashMap().withDefault(_ => random.nextFloat()))
//
//
//
//
////    new QLearingPlayer()
//    ???
//  }
//}

/**
  * Mutable Q-Learning Player
  */
class QLearingPlayer(var epsilon: Float = 0.2f, var alpha: Float = 0.3f, var gamma: Float = 0.9f, random: Random) extends Player{

  type State  = Table
  type Action = (Int, Int)

  // Q-Table is initialized by random float values
  val qTable: mutable.Map[State, mutable.Map[Action, Float]] = new mutable.HashMap().withDefaultValue(new mutable.HashMap().withDefaultValue(random.nextFloat()))


  private[this] var prevState: State   = null
  private[this] var prevAction: Action = null

  override def move(table: Table): (Int, Int) = {

    val action: Action = if(random.nextFloat() < epsilon){
      // random choice
      random.shuffle(table.emptyPoss).head
    } else {
      // best choice for now
      getBestAction(table)
    }

    // Set previous action
    prevAction = action
    prevState  = table

    action
  }

  /**
    * Get Best action
    * @param state
    * @return
    */
  def getBestAction(state: State): Action = {
    if(qTable(state).isEmpty){
      state.emptyPoss.head // TODO Change
    } else {
      val keyList = qTable(state).keys.toList
      val m = state.emptyPoss
        .filter(pos =>
          keyList.contains(pos)
        )
        .map{pos =>
          (pos, qTable(state)(pos))
        }
        .toMap
      if(m.isEmpty){
        state.emptyPoss.head
      } else {
        m.max._1
      }
    }
  }

  /**
    * Update Q-table (=learning this)
    * @param state
    * @param reward
    */
  def updateQ(state: State, reward: Float): Unit = {
    require(prevAction != null && prevState != null, "You must to call .move() before call .updateQ()")


    // This is for withDefault map (initialization)
    qTable(state)             = qTable(state)
    qTable(state)(prevAction) = qTable(state)(prevAction)

    val bestAction = getBestAction(prevState)
    qTable(state)(prevAction) = alpha * (reward + gamma * qTable(state)(bestAction) - qTable(state)(prevAction))
  }

}
