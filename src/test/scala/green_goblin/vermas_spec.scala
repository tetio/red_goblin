package green_goblin

import org.scalatest.BeforeAndAfterAll

class vermas_spec extends SpecHelper with BeforeAndAfterAll {

  override def beforeAll(): Unit = {
    securityToken = HttpOps.authenticate(username, password, companyCode)
    HttpOps.dbScript(securityToken, companyCode, "VGM")
  }

  describe("Green Goblin tries to sabotage the 'vermas' test") {
    it("doTestWait VERMAS/SOLICITUD-MODIFICACION should send 1 prepared messages and wait for them") {
      val messages = RedGoblin.throwPumpkin(securityToken, companyCode, MessageOps.loadMessages("messages/VERMAS/SOLICITUD-MODIFICACION"))
      expectTest(messages, 1)
    }
  }

  it("doTestWait VERMAS/COMUNICACION should send 1 prepared messages and wait for them") {
    val messages = RedGoblin.throwPumpkin(securityToken, companyCode, MessageOps.loadMessages("messages/VERMAS/COMUNICACION"))
    expectTest(messages, 1)
  }

  it("doTestWait VERMAS/COMUNICACION-MODIFICACION should send 1 prepared messages and wait for them") {
    val messages = RedGoblin.throwPumpkin(securityToken, companyCode, MessageOps.loadMessages("messages/VERMAS/COMUNICACION-MODIFICACION"))
    expectTest(messages, 1)
  }

  it("doTestWait VERMAS/COMUNICACION-CANCELACION should send 1 prepared messages and wait for them") {
    val messages = RedGoblin.throwPumpkin(securityToken, companyCode, MessageOps.loadMessages("messages/VERMAS/COMUNICACION-CANCELACION"))
    expectTest(messages, 1)
  }

  it("doTestWait VGM/SOLICITUD should send 1 prepared messages and wait for them") {
    val messages = RedGoblin.throwPumpkin(securityToken, companyCode, MessageOps.loadMessages("messages/VGM/SOLICITUD"))
    expectTest(messages, 1)
  }

  it("doTestWait VGM/ACEPTACION should send 1 prepared messages and wait for them") {
    val messages = RedGoblin.throwPumpkin(securityToken, companyCode, MessageOps.loadMessages("messages/VGM/ACEPTACION"))
    expectTest(messages, 1)
  }

  it("doTestWait VGM/SOLICITUD-KO should send 1 prepared messages and wait for them") {
    val messages = RedGoblin.throwPumpkin(securityToken, companyCode, MessageOps.loadMessages("messages/VGM/SOLICITUD-KO"))
    expectTest(messages, 1)
  }

  it("doTestWait VGM/SOLICITUD-MODIFICACION should send 1 prepared messages and wait for them") {
    val messages = RedGoblin.throwPumpkin(securityToken, companyCode, MessageOps.loadMessages("messages/VGM/SOLICITUD-MODIFICACION"))
    expectTest(messages, 1)
  }

  it("oTestWait VGM/COMUNICACION-KO should send 1 prepared messages and wait for them") {
    val messages = RedGoblin.throwPumpkin(securityToken, companyCode, MessageOps.loadMessages("messages/VGM/COMUNICACION-KO"))
    expectTest(messages, 1)
  }
}

