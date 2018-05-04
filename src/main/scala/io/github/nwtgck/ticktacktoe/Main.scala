package io.github.nwtgck.ticktacktoe

import io.github.nwtgck.ticktacktoe.player.{HumanPlayer, MinimaxPlayer, Player}

import scala.util.Random


/**
  * Created by Ryo on 29/5/17.
  */
object Main {

  type PlayerGenerator = (Random, Cell /** != Empty **/) => Player
  case class TickTackToeOptions(randomSeed : Int,
                                circlePlayerGenerator: PlayerGenerator,
                                crossPlayerGenerator:  PlayerGenerator
                               )

  def main(args: Array[String]): Unit = {

    // Default options
    val defaultOpts: TickTackToeOptions =
      TickTackToeOptions(
        randomSeed            = 10,
        circlePlayerGenerator = (random, turn) => new MinimaxPlayer(random, turn),
        crossPlayerGenerator  = (random, turn) => new MinimaxPlayer(random, turn)
      )

    // Option parser
    val parser = new scopt.OptionParser[TickTackToeOptions]("Tick Tack Toe") {
      opt[Int]("random-seed") action { (v, opts) =>
        opts.copy(randomSeed = v)
      } text s"random seed (default: ${defaultOpts.randomSeed})"

      opt[String]("circle") action { (v, opts) =>
        val playerGenerator: PlayerGenerator = v match {
          case "human"   => (_, _)         => new HumanPlayer()
          case "minimax" => (random, turn) => new MinimaxPlayer(random, turn)
        }
        opts.copy(circlePlayerGenerator = playerGenerator)
      } text s"""player of circle ("human" or "minimax")"""

      opt[String]("cross") action { (v, opts) =>
        val playerGenerator: PlayerGenerator = v match {
          case "human"   => (_, _)         => new HumanPlayer()
          case "minimax" => (random, turn) => new MinimaxPlayer(random, turn)
        }
        opts.copy(crossPlayerGenerator = playerGenerator)
      } text s"""player of cross  ("human" or "minimax")"""
    }

    parser.parse(args, defaultOpts) match {
      case Some(options) =>
        // Get random seed
        val randomSeed: Int = options.randomSeed
        val random = new scala.util.Random(seed = randomSeed)

        // Generate players
        val player1: Player = options.circlePlayerGenerator(random, Circle)
        val player2 : Player = options.crossPlayerGenerator(random, Cross)

        // Start game
        GameOrganizer.runGame(player1, player2, showTable = true, nPlays = 1)
      case None => ()
    }
  }

}
