package Syncordia.settings
import scala.collection.JavaConverters._
import scala.collection.mutable

/**
 * Created by Vadim Bryksin on 23/04/15.
 */
abstract class TestSettings {
  val initialHospitals: Int
  val initialCompanies: Int
  val initialDoctorsPerHospital: Int
  val initialPatients: Int
  val initialVisits: Int
  val prefix: String


  override def toString = s"TestSettings(initialHospitals=$initialHospitals, initialCompanies=$initialCompanies, initialDoctorsPerHospital=$initialDoctorsPerHospital, initialPatients=$initialPatients, initialVisits=$initialVisits, prefix=$prefix)"
}

class LoadSettings() extends TestSettings {
  private val env: mutable.Map[String, String] = System.getProperties.asScala
  private def getInt(name:String, default:Int) = {
    env.getOrElse(name, default.toString).toInt
  }
  override val initialHospitals = getInt("initialHospitals", 5000)
  override val initialCompanies = getInt("initialCompanies", 100)
  override val initialDoctorsPerHospital = getInt("initialDoctorsPerHospital", 200)
  override val initialPatients = getInt("initialPatients", 100000)
  override val initialVisits = getInt("initialVisits", 10)
  override val prefix = env.getOrElse("prefix", "")

}

class ExperimentSettings() extends TestSettings {
  override val initialHospitals = 50
  override val initialCompanies = 10
  override val initialDoctorsPerHospital = 20
  override val initialPatients = 100
  override val initialVisits = 3
  override val prefix: String = ""
}
