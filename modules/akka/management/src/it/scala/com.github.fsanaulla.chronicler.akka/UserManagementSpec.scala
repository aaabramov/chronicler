package com.github.fsanaulla.chronicler.akka

import _root_.akka.actor.ActorSystem
import _root_.akka.testkit.TestKit
import com.github.fsanaulla.chronicler.akka.management.{AkkaManagementClient, InfluxMng}
import com.github.fsanaulla.chronicler.core.enums.Privileges
import com.github.fsanaulla.chronicler.core.model.{UserInfo, UserPrivilegesInfo}
import com.github.fsanaulla.chronicler.testing.it.{DockerizedInfluxDB, Futures}
import org.scalatest.{FlatSpecLike, Matchers}

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by
  * Author: fayaz.sanaulla@gmail.com
  * Date: 10.08.17
  */
class UserManagementSpec
  extends TestKit(ActorSystem())
    with FlatSpecLike
    with Matchers
    with Futures
    with DockerizedInfluxDB {

  val userDB = "db"
  val userName = "Martin"
  val userPass = "pass"
  val userNPass = "new_pass"

  val admin = "Admin"
  val adminPass = "admin_pass"

  lazy val influx: AkkaManagementClient =
    InfluxMng(host, port, Some(creds))

  "User Management API" should "create user" in {
    influx.createDatabase(userDB).futureValue.right.get shouldEqual 200

    influx.createUser(userName, userPass).futureValue.right.get shouldEqual 200
    influx.showUsers.futureValue.right.get.contains(UserInfo(userName, isAdmin = false)) shouldEqual true
  }

  it should "create admin" in {
    influx.createAdmin(admin, adminPass).futureValue.right.get shouldEqual 200
    influx.showUsers.futureValue.right.get.contains(UserInfo(admin, isAdmin = true)) shouldEqual true
  }

  it should "show user privileges" in {
    influx.showUserPrivileges(admin).futureValue.right.get shouldEqual Nil
  }

  it should "set user password" in {
    influx.setUserPassword(userName, userNPass).futureValue.right.get shouldEqual 200
  }

  it should "set privileges" in {
    influx.setPrivileges(userName, userDB, Privileges.READ).futureValue.right.get shouldEqual 200
    influx.setPrivileges("unknown", userDB, Privileges.READ).futureValue.left.get.getMessage shouldEqual "user not found"

    influx.showUserPrivileges(userName).futureValue.right.get shouldEqual Array(UserPrivilegesInfo(userDB, Privileges.READ))
  }

  it should "revoke privileges" in {
    influx.revokePrivileges(userName, userDB, Privileges.READ).futureValue.right.get shouldEqual 200
    influx.showUserPrivileges(userName).futureValue.right.get shouldEqual Array(UserPrivilegesInfo(userDB, Privileges.NO_PRIVILEGES))
  }

  it should "disable admin" in {
    influx.disableAdmin(admin).futureValue.right.get shouldEqual 200
    influx.showUsers.futureValue.right.get.contains(UserInfo(admin, isAdmin = false)) shouldEqual true
  }

  it should "make admin" in {
    influx.makeAdmin(admin).futureValue.right.get shouldEqual 200
    influx.showUsers.futureValue.right.get.contains(UserInfo(admin, isAdmin = true)) shouldEqual true
  }

  it should "drop users" in {
    influx.dropUser(userName).futureValue.right.get shouldEqual 200
    influx.dropUser(admin).futureValue.right.get shouldEqual 200

    influx.close() shouldEqual {}
  }
}
