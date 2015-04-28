package test

import org.nohope.test.TRandom

import scala.collection.mutable.HashMap

/**
 * Created by Vadim Bryksin on 27/04/15.
 */
class Generator {

  val random = TRandom.threadLocal()
  private def genString(name: String):String = {
    val sb = new StringBuilder()
    sb.append("name : ").append("'").append(name).append("', ")

    for (x <- 1 to 5) {
      //size of 118 is made up as: originally key and val was 64:64,
      //i made key static - size of 10, but compensate in value: 64+54
      sb.append(s"Property_${x}").append(" : ").append("'").append(random.nextString(118, "Aa")).append("', ")
    }
    sb.setLength(sb.length-2)
    return sb.toString()
  }

  def generatePatient(name: String) = {
    genString(name)
  }

  def generateCompany(name: String) = {
    genString(name)
  }

  def generateInsuarance(name: String) = {
    genString(name)
  }


  def generateHospital(name: String) = {
    genString(name)
  }

  def generateDoctor(name: String) = {
    genString(name)
  }

  def generateVisit(name: String) = {
    genString(name)
  }

  def generateNotification(name: String) = {
    genString(name)
  }

  def generatePayment(name: String) = {
    genString(name)
  }

  def generateRejection(name: String) = {
    genString(name)
  }

}
