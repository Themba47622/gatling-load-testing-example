package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration.DurationInt

class Example4 extends Simulation {
  before {
    println(s"Running test with ${userCount} users")
    println(s"Ramping users over ${rampDuration} seconds")
    println(s"Total Test duration: ${testDuration} seconds")
  }

  /** Configure Protocol * */
  val httpConf = http
    .baseUrl("https://test-api.k6.io")
  //.proxy(Proxy("localhost", 8888).httpsPort(8888))

  /** Runtime Parameters * */
  def userCount: Int = getProperty("USERS", "3").toInt

  def rampDuration: Int = getProperty("RAMP_DURATION", "3").toInt

  def testDuration: Int = getProperty("DURATION", "30").toInt

  /** Helper Methods * */
  private def getProperty(propertyName: String, defaultValue: String) = {
    Option(System.getenv(propertyName))
      .orElse(Option(System.getProperty(propertyName)))
      .getOrElse(defaultValue)
  }

  /** Test Data CSV * */
  val csvFeeder = csv("data/crocodiles.csv").circular

  /** API Calls * */
  def postVodAuth() = {
    feed(csvFeeder).
      exec(http("POST /public/crocodiles/")
        .post("/public/crocodiles/${id}")
        .check(status.is(200)))
  }

  def getCrocodiles() = {
    val path: String = "/public/crocodiles/"
    exec(http("GET /public/crocodiles/")
      .get(path)
      .check(status.is(200)))
  }

  /** Scenarios * */
  val scn = scenario("Entitlements")
    .forever() {
      exec(postVodAuth())
        .pause(1)
        .exec(getCrocodiles())
        .pause(1)
    }

  /** Load Test Simulation * */
  setUp(
    scn.inject(
      nothingFor(0 seconds),
      rampUsers(userCount) during (rampDuration seconds))
  )
    .protocols(httpConf)
    .maxDuration(testDuration seconds)

  after {
    println("Load Test completed")
  }
}
