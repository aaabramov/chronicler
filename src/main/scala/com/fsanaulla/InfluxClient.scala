package com.fsanaulla

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.fsanaulla.api.{DatabaseManagement, UserManagement}
import com.fsanaulla.model.TypeAlias._

import scala.concurrent.ExecutionContext

/**
  * Created by fayaz on 26.06.17.
  */
class InfluxClient(host: String,
                   port: Int = 8086,
                   username: Option[String] = None,
                   password: Option[String] = None)
                  (implicit val ex: ExecutionContext)
  extends DatabaseManagement
  with UserManagement {

  implicit val system = ActorSystem("system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val connection: ConnectionPoint = Http().outgoingConnection(host, port)
}
