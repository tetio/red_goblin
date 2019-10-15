package green_goblin

import org.scalatest.{BeforeAndAfterAll, FunSpec}


class sample_spec extends SpecHelper  with BeforeAndAfterAll {

  var secutityToken = ""
  val username = "aaaa" // found in resources file
  val companyCode = "aaaa" // found in resources file
  val password = "aaaa" // found in resourcesv file

  override def beforeAll(): Unit = {
    secutityToken = HttpOps.authenticate(username, password, companyCode)
    HttpOps.dbScript(secutityToken, companyCode, "data.sql")
  }

  describe("Green Goblin tries to sabotage the mta subsystem") {

    it("Throw pumpking at sample") {
      val messages = RedGoblin.throwPumpkin(secutityToken, companyCode, MessageOps.loadMessages("/messages/sample"))
      expectTest(messages, 2)
    }

    it("Final check") {
      finalCheck()
    }
  }
}
