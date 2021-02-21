package simulations

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._

import scala.concurrent.duration._

class Example3 extends Simulation {
  /** Configure Protocol * */
  val httpConf = http.baseUrl("https://test-api.k6.io")
    .header("Accept", "application/json")

  /** Scenarios * */
  val scn = scenario("request croc info")

    .exec(http("GET /public/crocodiles/")
      .get("/public/crocodiles/")
      .check(status.is(200))
      .check(jsonPath("$[1].id").saveAs("id")))
    .exec { session => println(session); session }

    .exec(http("GET /public/crocodiles/${id}")
      .get("/public/crocodiles/${id}")
      .check(status.is(200))
      .check(bodyString.saveAs("responseBody")))
    .exec { session => println(session("responseBody").as[String]); session }

  /** Load Test Simulation * */
  setUp(
    scn.inject(
      incrementConcurrentUsers(5)
        .times(5)
        .eachLevelLasting(10 seconds)
        .separatedByRampsLasting(0 seconds)
        .startingFrom(10)
    )
  ).protocols(httpConf)
    .maxDuration(30 seconds)
}
