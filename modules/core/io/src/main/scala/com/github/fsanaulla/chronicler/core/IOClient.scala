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

package com.github.fsanaulla.chronicler.core

import com.github.fsanaulla.chronicler.core.api.{DatabaseApi, MeasurementApi}
import com.github.fsanaulla.chronicler.core.management.SystemManagement

import scala.reflect.ClassTag

/**
  * Define necessary methods for providing IO operations
  *
  * @tparam F - Response type container
  */
trait IOClient[F[_], Resp, Uri, Body] extends SystemManagement[F] with AutoCloseable {

  type Database       = DatabaseApi[F, Resp, Uri, Body]
  type Measurement[A] = MeasurementApi[F, Resp, Uri, Body, A]

  /**
    * Get database instant
    *
    * @param dbName - database name
    * @return       - Backend related implementation of DatabaseIO
    */
  def database(dbName: String): Database

  /**
    * Get measurement instance with execution type A
    *
    * @param dbName          - on which database
    * @param measurementName - which measurement
    * @tparam A              - measurement entity type
    * @return                - Backend related implementation of MeasurementIO
    */
  def measurement[A: ClassTag](
      dbName: String,
      measurementName: String
    ): MeasurementApi[F, Resp, Uri, Body, A]
}
