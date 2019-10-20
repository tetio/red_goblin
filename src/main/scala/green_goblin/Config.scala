package green_goblin

object Config {

  def config(): Map[String, String] = {
    val env = System.getProperty("PORTIC_ENV")
    env match {
      case "local" => Map("ip" -> "localhost", "port" -> "4000")
      case "desa" => Map("ip" -> "10.120.1.182", "port" -> "12100")
      case "test" => Map("ip" -> "reingtest.portic.net", "port" -> "80")
      case _ => Map("ip" -> "localhost", "port" -> "4000")
    }
  }

  def ip = config()("ip")
  def port = config()("port")
}
