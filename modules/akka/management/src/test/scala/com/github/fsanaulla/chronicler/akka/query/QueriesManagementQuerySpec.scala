/*
 * Copyright 2017-2019 Faiaz Sanaulla
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.fsanaulla.chronicler.akka.query

import com.github.fsanaulla.chronicler.akka.shared.handlers.AkkaQueryBuilder
import com.github.fsanaulla.chronicler.core.model.InfluxCredentials
import com.github.fsanaulla.chronicler.core.query.QueriesManagementQuery
import com.softwaremill.sttp.Uri
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by
  * Author: fayaz.sanaulla@gmail.com
  * Date: 20.08.17
  */
class QueriesManagementQuerySpec extends FlatSpec with Matchers with QueriesManagementQuery[Uri] {

  trait AuthEnv {
    val credentials                   = Some(InfluxCredentials("admin", "admin"))
    implicit val qb: AkkaQueryBuilder = new AkkaQueryBuilder("localhost", 8086, credentials)
  }

  trait NonAuthEnv {
    implicit val qb: AkkaQueryBuilder = new AkkaQueryBuilder("localhost", 8086, None)
  }

  "QueryManagement" should "show query" in new AuthEnv {
    showQuerysQuery.toString() shouldEqual queryTesterAuth("SHOW QUERIES")(credentials.get)
  }

  it should "kill query" in new AuthEnv {
    killQueryQuery(5).toString() shouldEqual queryTesterAuth("KILL QUERY 5")(credentials.get)
  }

  it should "show query without auth" in new NonAuthEnv {
    showQuerysQuery.toString() shouldEqual queryTester("SHOW QUERIES")
  }

  it should "kill query without auth" in new NonAuthEnv {
    killQueryQuery(5).toString() shouldEqual queryTester("KILL QUERY 5")
  }
}
