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

package com.github.fsanaulla.chronicler.core.management

import com.github.fsanaulla.chronicler.core.alias.{ErrorOr, ResponseCode}
import com.github.fsanaulla.chronicler.core.components._
import com.github.fsanaulla.chronicler.core.implicits._
import com.github.fsanaulla.chronicler.core.model._
import com.github.fsanaulla.chronicler.core.query.ContinuousQueries

/**
  * Created by
  * Author: fayaz.sanaulla@gmail.com
  * Date: 08.08.17
  */
trait ContinuousQueryManagement[F[_], Resp, Uri, Entity] extends ContinuousQueries[Uri] {
  implicit val qb: QueryBuilder[Uri]
  implicit val re: RequestExecutor[F, Resp, Uri, Entity]
  implicit val rh: ResponseHandler[Resp]
  implicit val F: Functor[F]

  /**
    * Create new one continuous query
    *
    * @param dbName - database on which CQ will runes
    * @param cqName - continuous query name
    * @param query  - query
    * @return
    */
  final def createCQ(
      dbName: String,
      cqName: String,
      query: String
    ): F[ErrorOr[ResponseCode]] = {
    require(validCQQuery(query), "Query required INTO and GROUP BY clause")
    F.map(re.get(createCQQuery(dbName, cqName, query)))(rh.writeResult)
  }

  /** Show continuous query information */
  final def showCQs: F[ErrorOr[Array[ContinuousQueryInfo]]] =
    F.map(re.get(showCQQuery))(rh.toCqQueryResult)

  /**
    * Drop continuous query
    *
    * @param dbName - database name
    * @param cqName - continuous query name
    * @return       - execution result
    */
  final def dropCQ(dbName: String, cqName: String): F[ErrorOr[ResponseCode]] =
    F.map(re.get(dropCQQuery(dbName, cqName)))(rh.writeResult)

  private[this] def validCQQuery(query: String): Boolean =
    query.contains("INTO") && query.contains("GROUP BY")
}
