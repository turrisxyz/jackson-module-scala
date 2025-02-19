package com.fasterxml.jackson.module.scala.deser

import com.fasterxml.jackson.module.scala.OuterWeekday.InnerWeekday
import com.fasterxml.jackson.module.scala.ser.EnumerationSerializerTest.{AnnotationHolder, AnnotationOptionHolder, WeekdayType}
import com.fasterxml.jackson.module.scala.{DefaultScalaModule, JsonScalaEnumeration, Weekday}

import scala.beans.BeanProperty

class EnumContainer {
  var day: Weekday.Value = Weekday.Fri
}

class EnumMapHolder {
  @JsonScalaEnumeration(classOf[WeekdayType])
  var weekdayMap: Map[Weekday.Value, String] = Map.empty
}

object EnumerationDeserializerTest  {
  trait BeanPropertyEnumMapHolder {
    @BeanProperty
    @JsonScalaEnumeration(classOf[WeekdayType])
    var weekdayMap: Map[Weekday.Value, String] = Map.empty
  }

  class HolderImpl extends BeanPropertyEnumMapHolder
}

// see Json for tests that only in Scala2
class EnumerationDeserializerTest extends DeserializerTest {

  lazy val module: DefaultScalaModule.type = DefaultScalaModule

  "An ObjectMapper with EnumDeserializerModule" should "deserialize a value into a scala Enumeration as a bean property" in {
    val expectedDay = Weekday.Fri
    val result = deserialize(fridayEnumJson, classOf[EnumContainer])
    result.day should be (expectedDay)
  }

  "An ObjectMapper with EnumDeserializerModule" should "deserialize a value of an inner Enumeration class into a scala Enumeration as a bean property" in {
    val expectedDay = InnerWeekday.Fri
    val result = deserialize(fridayInnerEnumJson, classOf[EnumContainer])
    result.day should be (expectedDay)
  }

  it should "deserialize an annotated Enumeration value (JsonScalaEnumeration)" in {
    val result = deserialize(annotatedFridayJson, classOf[AnnotationHolder])
    result.weekday should be (Weekday.Fri)
  }

  it should "deserialize an annotated optional Enumeration value (JsonScalaEnumeration)" in {
    val result = deserialize(annotatedFridayJson, classOf[AnnotationOptionHolder])
    result.weekday shouldBe Some(Weekday.Fri)
  }

  val fridayEnumJson = """{"day": {"enumClass":"com.fasterxml.jackson.module.scala.Weekday","value":"Fri"}}"""

  val fridayInnerEnumJson = """{"day": {"enumClass":"com.fasterxml.jackson.module.scala.OuterWeekday$InnerWeekday","value":"Fri"}}"""

  val annotatedFridayJson = """{"weekday":"Fri"}"""
}
