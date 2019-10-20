package green_goblin

import org.scalatest.BeforeAndAfterAll


class sample_spec extends SpecHelper with BeforeAndAfterAll {

  var securityToken = ""
  val username = "NESTA" // found in resources file
  val companyCode = "ESA61961488" // found in resources file
  val password = "CALL" // found in resourcesv file

  override def beforeAll(): Unit = {
    securityToken = HttpOps.authenticate(username, password, companyCode)
    HttpOps.dbScript(securityToken, companyCode, "data.sql")
  }

  describe("Green Goblin tries to sabotage the 'sample' test") {

    it("Throw pumpkin at sample") {
      val messages = RedGoblin.throwPumpkin(securityToken, companyCode, MessageOps.loadMessages("/messages/sample"))
      expectTest(messages, 2)
    }
  }

  describe("Green Goblin tries to sabotage the same 'sample' test") {

    it("Throw pumpkin at the same sample") {
      val messages = RedGoblin.throwPumpkin(securityToken, companyCode, MessageOps.loadMessages("/messages/sample"))
      expectTest(messages, 2)
    }
  }
}
