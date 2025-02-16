package com.github.fsanaulla.chronicler.akka

import com.github.fsanaulla.chronicler.testing.it.FakeEntity
import org.typelevel.jawn.ast.{JArray, JNum, JString}

object SampleEntitys {

  final val currentNanoTime: Long = System.currentTimeMillis() * 1000000

  // INTEGRATION SPEC ENTITYS
  val singleEntity = FakeEntity("Male", "Martin", "Odersky", 58)

  val singleJsonEntity =
    JArray(Array(
      JNum(currentNanoTime),
      JNum(58),
      JString("Martin"),
      JString("Odersky"))
    )

  val multiEntitys = Array(FakeEntity("Male", "Harold", "Lois", 44), FakeEntity("Male", "Harry", "Potter", 21))

  val multiJsonEntity = Array(
    JArray(Array(
      JNum(currentNanoTime),
      JNum(58),
      JString("Martin"),
      JString("Odersky"))),
    JArray(Array(
      JNum(currentNanoTime),
      JNum(44),
      JString("Harold"),
      JString("Lois"))),
    JArray(Array(
      JNum(currentNanoTime),
      JNum(21),
      JString("Harry"),
      JString("Potter"))
    )
  )

  val largeMultiJsonEntity = Array(
    Array(
      JArray(Array(
        JNum(currentNanoTime),
        JNum(54),
        JString("Martin"),
        JString("Odersky"),
        JString("Male"))),
      JArray(Array(
        JNum(currentNanoTime),
        JNum(36),
        JString("Jame"),
        JString("Franko"),
        JString("Male"))),
      JArray(Array(
        JNum(currentNanoTime),
        JNum(54),
        JString("Martin"),
        JString("Odersky"),
        JString("Male")))
    ),
    Array(
      JArray(Array(
        JNum(currentNanoTime),
        JNum(36),
        JString("Jame"),
        JString("Franko"),
        JString("Male")))
    )
  )

  // UNIT SPEC ENTITYS
  val singleResult: Array[JArray] = Array(
    JArray(Array(
      JString("2015-01-29T21:55:43.702900257Z"),
      JNum(2))),
    JArray(Array(
      JString("2015-01-29T21:55:43.702900257Z"),
      JNum(0.55))),
    JArray(Array(
      JString("2015-06-11T20:46:02Z"),
      JNum(0.64)))
  )

  val bulkResult = Array(
    Array(
      JArray(Array(
        JString("2015-01-29T21:55:43.702900257Z"),
        JNum(2))),
      JArray(Array(
        JString("2015-01-29T21:55:43.702900257Z"),
        JNum(0.55))),
      JArray(Array(
        JString("2015-06-11T20:46:02Z"),
        JNum(0.64)))
    ),
    Array(
      JArray(Array(
        JString("2015-01-29T21:55:43.702900257Z"),
        JNum(2))),
      JArray(Array(
        JString("2015-01-29T21:55:43.702900257Z"),
        JNum(0.55)))
    )
  )
}
