package green_goblin

object Config {

  def config(): Map[String, String] = {
    val env = System.getenv("PORTIC_ENV")
    env match {
      case "local" => Map("ip" -> "localhost", "port" -> "4000")
      case "desa" => Map("ip" -> "10.120.1.182", "port" -> "12100")
      //case "test" => Map("ip" -> "10.120.1.176", "port" -> "11100")
      case _ => Map("ip" -> "10.120.1.176", "port" -> "11100")
    }
  }

  def apply(key: String) = config()(key)
//  def ip: String = config()("ip")
//  def port: String = config()("port")
}
