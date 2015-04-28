package test

import java.sql._

import test.settings._
import com.typesafe.scalalogging.StrictLogging
import org.nohope.test.stress.action.Invoke
import org.nohope.test.stress.{Action, MeasureProvider, TimerResolution, StressScenario}

import scala.util.control.NonFatal

/**
 * Created by Vadim Bryksin on 24/04/15.
 */
object DatabaseManager extends StrictLogging {

  private var db: DbData = null
  private var settings: TestSettings = null
  private var gen: Generator = null
  private var con: Connection = null

  def initDb(isLocalTest: Boolean) = {

    if (isLocalTest)
      settings = new ExperimentSettings
    else
      settings = new LoadSettings

    db = new LoadData
    gen = new Generator

    Class.forName("org.neo4j.jdbc.Driver")
  }

  def getConnection() : Connection = {
    if (con != null && !con.isClosed()) {
      return con
    } else {
      try {

        logger.info("Connecting to: " +db.remoteUrl)
        con = DriverManager.getConnection(db.remoteUrl, db.user, db.pass)
        logger.info("Connected")
        return con
      } catch {
        case e: Throwable =>
          logger.error("Failed to connect to database: ", e)
          logger.info("Terminating application...")
          System.exit(0)
          return null
      }
    }
  }

  def clearDatabase() = {
    val conn = getConnection()
    logger.info("\nClearing database...")
    val query: String = "MATCH (n) OPTIONAL MATCH (n)-[r]-()DELETE n,r"
    try {
      conn.prepareStatement(query).executeQuery()
      logger.info("Database wiped\n")
    }
    catch {
      case e: Throwable =>
        logger.error(s"Failed to execute query: $query",  e)
    }
  }

  def fillDatabase() = {

    val fillResult =
      StressScenario.of(TimerResolution.NANOSECONDS)
        .measure(db.concurrency, 1, new Action {
        override def doAction(p: MeasureProvider) = {
          p.invoke("Load Database With Random Data", new Invoke {
            override def invoke() = {

              val opNumber = p.getOperationNumber()

              try {
                logger.info(s"Thread:$opNumber - Generating Companies...")

                for (x <- 0 to settings.initialCompanies / db.concurrency) {
                  val companyProps = gen.generateCompany(s"company_${x}_$opNumber")
                  val query = s"CREATE(n:insurance_company { $companyProps })"
                  getConnection().prepareStatement(query).executeQuery()
                }
              } catch {
                case NonFatal(e) =>
                  logger.error(s"Company insert error: ", e)
              }

              try {
                logger.info(s"Thread:$opNumber - Generating Hospitals...")

                for (x <- 0 to settings.initialHospitals / db.concurrency) {
                  val hospitalName = s"hospital_${x}_${opNumber}"
                  val hospitalProps = gen.generateHospital(hospitalName)
                  val query = s"CREATE(n:hospital { $hospitalProps })"
                  getConnection().prepareStatement(query).executeQuery()

                  logger.info(s"Thread:$opNumber - Generating Doctors for hospital: $hospitalName")

                  for (y <- 0 to settings.initialDoctorsPerHospital) {
                    val docName = s"doctor_${y}_from_hospital_${x}_$opNumber"
                    val doctorProps = gen.generateDoctor(docName)
                    var query = s"CREATE(n:doctor { $doctorProps })"
                    getConnection().prepareStatement(query).executeQuery()

                    query = s"MATCH (a:doctor),(b:hospital) WHERE a.name = '$docName' AND b.name = '$hospitalName' CREATE (a)-[r:employed_at]->(b)"
                    getConnection().prepareStatement(query).executeQuery()
                  }
                }
              } catch {
              case NonFatal(e) =>
                logger.error(s"Hospitals and Doctors insert error: ", e)
              }

              try {
                logger.info(s"Thread:$opNumber - Generating Patients...")

                for (x <- 0 to settings.initialPatients / db.concurrency) {
                  val patientName = s"patient_${x}_${opNumber}"
                  val patientProps = gen.generatePatient(patientName)
                  val query = s"CREATE(n:patient { $patientProps })"
                  getConnection().prepareStatement(query).executeQuery()

                  logger.info(s"Thread:$opNumber - Generating Visits for Patient: $patientName")

                  for (y <- 0 to settings.initialVisits) {
                    val visitName = s"${patientName}_visit_$y"
                    val visitProps = gen.generateVisit(visitName)
                    var query = s"CREATE(n:visit { $visitProps })"
                    getConnection().prepareStatement(query).executeQuery()

                    query = s"MATCH (a:patient),(b:visit) WHERE a.name = '$patientName' AND b.name = '$visitName' CREATE (a)-[r:has]->(b)"
                    getConnection().prepareStatement(query).executeQuery()
                  }
                }

              } catch {
                case NonFatal(e) =>
                  logger.error(s"Patients and Visit insert error: ", e)
              }

              logger.info(s"Thread:$opNumber - Generation Complete")
            }

          })
        }
      })
    logger.info(s"Initial setup results:\n$fillResult")

  }


}
