package test

import com.typesafe.scalalogging.StrictLogging

/**
 * Created by Vadim Bryksin on 23/04/15.
 */
object Tester extends App with StrictLogging{

  override def main(args: Array[String]) = {

    logger.info("Supported args: localtest, clear, fill, rw")
    val argsSet: Set[String] = args.toSet

    if (argsSet.contains("localtest")) {
      DatabaseManager.initDb(true)
    } else {
      DatabaseManager.initDb(false)
    }

    if (argsSet.contains("clear")) {
      DatabaseManager.clearDatabase()
    }

    if (argsSet.contains("fill")) {
      DatabaseManager.fillDatabase()
    }
    if (argsSet.contains("rw")) {

    }
  }
}
