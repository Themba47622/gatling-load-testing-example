package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class Example2 extends Simulation {

  /** Configure Protocol * */
  val httpConf = http
    .baseUrl("https://test-api.k6.io")
    .acceptHeader("*/*")
    .contentTypeHeader("application/json")

  /** Runtime Parameters * */
  def userCount: Int = getProperty("USERS", "5").toInt

  def rampDuration: Int = getProperty("RAMP_DURATION", "5").toInt

  def testDuration: Int = getProperty("DURATION", "10").toInt

  /** Helper Methods * */
  private def getProperty(propertyName: String, defaultValue: String) = {
    Option(System.getenv(propertyName))
      .orElse(Option(System.getProperty(propertyName)))
      .getOrElse(defaultValue)
  }

  /** API Calls * */
  object CrocodilesAPI {
    val feeder = csv("data/crocodiles.csv").random

    val crocs = forever() {
      feed(feeder)
        .exec(http("GET /public/crocodiles/")
          .get("/public/crocodiles/")
          .check(status.is(200)))

        .exec(http("GET /public/crocodiles/${id}")
          .get("/public/crocodiles/${id}")
          .check(status.is(200)))
    }
  }

  /** Scenarios * */
  val reqCrocInfo = scenario("Api calls to /public/crocodiles").exec(CrocodilesAPI.crocs)

  /** Load Simulation * */
  setUp(
    reqCrocInfo.inject(rampUsers(userCount) during (rampDuration seconds))
  ).protocols(httpConf)
    .maxDuration(testDuration seconds)
}