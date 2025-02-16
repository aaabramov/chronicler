package com.github.fsanaulla.chronicler.urlhttp

import com.github.fsanaulla.chronicler.testing.it.{DockerizedInfluxDB, Futures}
import com.github.fsanaulla.chronicler.urlhttp.management.{InfluxMng, UrlManagementClient}
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by
  * Author: fayaz.sanaulla@gmail.com
  * Date: 07.09.17
  */
class SystemManagementSpec
  extends FlatSpec
    with Matchers
    with Futures
    with DockerizedInfluxDB {

  lazy val influx: UrlManagementClient =
    InfluxMng(host, port, Some(creds))

  "System Management API" should "ping InfluxDB" in {
    val result = influx.ping.get.right.get
    result.build shouldEqual "OSS"
    result.version shouldEqual version
  }
}
