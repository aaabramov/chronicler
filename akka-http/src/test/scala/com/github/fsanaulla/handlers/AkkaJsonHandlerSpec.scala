package com.github.fsanaulla.handlers

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpEntity, HttpResponse}
import akka.stream.ActorMaterializer
import com.github.fsanaulla.TestSpec
import com.github.fsanaulla.utils.AkkaContentTypes.AppJson
import spray.json.{JsObject, JsonParser}

class AkkaJsonHandlerSpec extends TestSpec with AkkaJsonHandler {

  implicit val system: ActorSystem = ActorSystem("TestOne")
  implicit val mat: ActorMaterializer = ActorMaterializer()

  val singleStrJson = """{
                      "results": [
                          {
                              "statement_id": 0,
                              "series": [
                                 {
                                      "name": "cpu_load_short",
                                      "columns": [
                                          "time",
                                          "value"
                                      ],
                                      "values": [
                                          [
                                              "2015-01-29T21:55:43.702900257Z",
                                              2
                                          ],
                                          [
                                              "2015-01-29T21:55:43.702900257Z",
                                              0.55
                                          ],
                                          [
                                              "2015-06-11T20:46:02Z",
                                              0.64
                                          ]
                                      ]
                                  }
                              ]
                          }
                      ]
                  }"""

  val singleHttpResponse: HttpResponse = HttpResponse(entity = HttpEntity(AppJson, singleStrJson))

  val jsResult: JsObject = JsonParser(singleStrJson).asJsObject

  "Akka json handler" should "extract js object from HTTP response" in {
    getJsBody(singleHttpResponse).futureValue shouldEqual jsResult
  }
}
