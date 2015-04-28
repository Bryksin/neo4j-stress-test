package test.settings
import scala.collection.JavaConverters._
import scala.collection.mutable

/**
 * Created by Vadim Bryksin on 23/04/15.
 */
abstract class DbData {
  val host: String
  val port: String
  val user: String
  val pass: String
  val remoteUrl: String
  val concurrency: Int
  val prefix: String = "jdbc:neo4j:"

  override def toString = s"DbData(host=$host, port=$port, user=$user, pass=$pass, remoteUrl=$remoteUrl, concurrency=$concurrency)"
}

class LoadData extends DbData {
  private val env: mutable.Map[String, String] = System.getProperties.asScala

  override val host = env.getOrElse("host", "localhost")
  override val port = env.getOrElse("port", "7474")
  override val user = env.getOrElse("user", "neo4j")
  override val pass = env.getOrElse("pass", "Pa88w0rd")
  override val concurrency = env.getOrElse("concurrency", Runtime.getRuntime.availableProcessors().toString).toInt
  val remoteUrl = env.getOrElse("uri", s"$prefix//$host:$port/")

}

