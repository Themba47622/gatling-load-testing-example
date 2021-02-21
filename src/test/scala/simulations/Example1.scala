package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class Example1 extends Simulation {
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
    scn.inject(atOnceUsers(5))
  ).protocols(httpConf)
}
