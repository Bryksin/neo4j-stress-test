package Syncordia

import Syncordia.settings.{LoadSettings, LoadData}
import com.typesafe.scalalogging.StrictLogging

/**
 * Created by Vadim Bryksin on 23/04/15.
 */
object Tester extends App with StrictLogging{

  override def main(args: Array[String]) = {

    logger.info("Supported args: clear, fill, rw")

    DatabaseManager.initDb()
    DatabaseManager.clearDatabase()
    DatabaseManager.fillDatabase()

    val argsSet: Set[String] = args.toSet
    if (argsSet.contains("clear")) {
      DatabaseManager.clearDatabase()
    }

    if (argsSet.contains("fill")) {

    }
    if (argsSet.contains("rw")) {

    }
  }
}
