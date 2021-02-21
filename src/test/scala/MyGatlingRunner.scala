import io.gatling.app.Gatling
import io.gatling.core.config.GatlingPropertiesBuilder
import simulations.Example1

object MyGatlingRunner {

  def main(args: Array[String]): Unit = {

    val simClass = classOf[Example1].getName

    val props = new GatlingPropertiesBuilder
    props.simulationClass(simClass)

    Gatling.fromMap(props.build)
  }
}
