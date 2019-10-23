package green_goblin

import org.scalatest.BeforeAndAfterAll


class sample_spec extends SpecHelper with BeforeAndAfterAll {

  override def beforeAll(): Unit = {
    securityToken = HttpOps.authenticate(username, password, companyCode)
    note(s"token: ${securityToken}")
    val resultDBScript = HttpOps.dbScript(securityToken, companyCode, "data")
    note(s"dbScript: ${resultDBScript}")
  }

  describe("Green Goblin tries to sabotage the 'sample' test") {

    it("Throw pumpkin at sample") {
      val messages = RedGoblin.throwPumpkin(securityToken, companyCode, MessageOps.loadMessages("/sample"))
      expectTest(messages, 2)
    }
  }

  describe("Green Goblin tries to sabotage the same 'sample' test") {

    it("Throw pumpkin at the same sample") {
      val messages = RedGoblin.throwPumpkin(securityToken, companyCode, MessageOps.loadMessages("/sample"))
      expectTest(messages, 2)
    }
  }
}
