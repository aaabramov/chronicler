package com.github.fsanaulla.chronicler.akka

import _root_.akka.actor.ActorSystem
import _root_.akka.testkit.TestKit
import com.github.fsanaulla.chronicler.akka.SampleEntitys._
import com.github.fsanaulla.chronicler.akka.io.{AkkaIOClient, InfluxIO}
import com.github.fsanaulla.chronicler.akka.management.{AkkaManagementClient, InfluxMng}
import com.github.fsanaulla.chronicler.akka.shared.InfluxConfig
import com.github.fsanaulla.chronicler.testing.it.{DockerizedInfluxDB, FakeEntity, Futures}
import org.scalatest.{FlatSpecLike, Matchers}

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by
  * Author: fayaz.sanaulla@gmail.com
  * Date: 28.09.17
  */
class MeasurementApiSpec
  extends TestKit(ActorSystem())
    with FlatSpecLike
    with Matchers
    with Futures
    with DockerizedInfluxDB {

  val db = "db"
  val measName = "meas"

  lazy val influxConf =
    InfluxConfig(host, port, credentials = Some(creds), gzipped = false, None)

  lazy val mng: AkkaManagementClient =
    InfluxMng(host, port, credentials = Some(creds))

  lazy val io: AkkaIOClient = InfluxIO(influxConf)
  lazy val meas: io.Measurement[FakeEntity] =
    io.measurement[FakeEntity](db, measName)

  it should "write single point" in {
    mng.createDatabase(db).futureValue.right.get shouldEqual 200

    meas.write(singleEntity).futureValue.right.get shouldEqual 204

    meas.read(s"SELECT * FROM $measName")
      .futureValue
      .right
      .get shouldEqual Seq(singleEntity)
  }

  it should "bulk write" in {
    meas.bulkWrite(multiEntitys).futureValue.right.get shouldEqual 204

    meas.read(s"SELECT * FROM $measName")
      .futureValue
      .right
      .get
      .length shouldEqual 3

    mng.close() shouldEqual {}
    io.close() shouldEqual {}
  }
}
