package green_goblin

import org.scalatest.FunSuite

class RedGoblinTest extends FunSuite {

  test("GreenGoblin throws a pumpkin to sample") {
    val messages = MessageOps.loadMessages("/messages/sample")
    val result = RedGoblin.throwPumpkin("tokenXYZ", "CAX7878786", messages)
    assert(result.size === 2)
  }

}
