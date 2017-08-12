package com.fsanaulla.integration

import com.fsanaulla.InfluxClient
import com.fsanaulla.model._
import com.fsanaulla.utils.Extension._
import com.fsanaulla.utils.SampleEntitys.{multiEntitys, singleEntity}
import com.fsanaulla.utils.TestHelper.{FakeEntity, NoContentResult, OkResult}
import com.fsanaulla.utils.TestSpec

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by
  * Author: fayaz.sanaulla@gmail.com
  * Date: 26.07.17
  */
class DataManagementSpec extends TestSpec {

  final val dbName = "data_db"

  "Data management operation" should "correctly work" in {
    val influx = InfluxClient(host)

    influx.createDatabase(dbName).futureValue shouldEqual OkResult

    influx.showDatabases().futureValue.queryResult.contains(DatabaseInfo(dbName)) shouldEqual true

    val db = influx.use(dbName)
    db.bulkWrite("meas1", multiEntitys).futureValue shouldEqual NoContentResult
    db.read[FakeEntity]("SELECT * FROM meas1").futureValue.queryResult shouldEqual multiEntitys

    db.write("meas2", singleEntity).futureValue shouldEqual NoContentResult
    db.read[FakeEntity]("SELECT * FROM meas2").futureValue.queryResult shouldEqual Seq(singleEntity)

    influx.showMeasurement(dbName).futureValue.queryResult shouldEqual Seq(MeasurementInfo("meas1"), MeasurementInfo("meas2"))

    influx.dropMeasurement(dbName, "meas1").futureValue shouldEqual OkResult

    db.read[FakeEntity]("SELECT * FROM meas1").futureValue.queryResult shouldEqual Nil

    influx.showMeasurement(dbName).futureValue.queryResult shouldEqual Seq(MeasurementInfo("meas2"))

    influx.dropDatabase(dbName).futureValue shouldEqual OkResult

    influx.showDatabases().futureValue.queryResult.contains(DatabaseInfo(dbName)) shouldEqual false

    influx.close()
  }
 }
