import org.scalatest.{FlatSpec}

class HttpOpsTest extends FlatSpec {

  "a dummy1 call" should "return hello" in {
    val response = HttpOps.ping()
    assert(response === "Hello!")
  }

}
