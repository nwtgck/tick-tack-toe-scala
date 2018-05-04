# Tick-Tack-Toe in Scala
[![Build Status](https://travis-ci.com/nwtgck/tick-tack-toe-scala.svg?branch=master)](https://travis-ci.com/nwtgck/tick-tack-toe-scala)

Tick-Tack-Toe written in Scala by using minimax


## Machine vs Machine

```bash
sbt run
```

## How to play


Random seed: 76  
Circle player: human  
Cross player : default ("minimax")  

```bash
sbt "runMain io.github.nwtgck.ticktacktoe.Main --random-seed=76 --circle=human"
```

## Available options

```txt
Usage: Tick Tack Toe [options]

  --random-seed <value>  random seed (default: 10)
  --circle <value>       player of circle ("human" or "minimax")
  --cross <value>        player of cross  ("human" or "minimax")
```
