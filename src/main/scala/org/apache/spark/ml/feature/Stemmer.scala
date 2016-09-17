package org.apache.spark.ml.feature

import org.tartarus.snowball.SnowballStemmer

import org.apache.spark.sql.types.{ DataType, StringType, ArrayType }
import org.apache.spark.ml.util.Identifiable
import org.apache.spark.ml.param.{ Param, ParamMap }
import org.apache.spark.ml.UnaryTransformer

class Stemmer(override val uid: String) extends UnaryTransformer[Seq[String], Seq[String], Stemmer] {

  def this() = this(Identifiable.randomUID("stemmer"))

  val language: Param[String] = new Param(this, "language", "stemming language (case insensitive).")
  def getLanguage: String = $(language)
  def setLanguage(value: String): this.type = set(language, value)

  setDefault(language -> "English")

  override protected def createTransformFunc: Seq[String] => Seq[String] = {
    val stemClass = Class.forName("org.tartarus.snowball.ext." + $(language).toLowerCase + "Stemmer")
    val stemmer = stemClass.newInstance.asInstanceOf[SnowballStemmer]
    originStr =>
      try {
        originStr.map { x =>
          stemmer.setCurrent(x)
          stemmer.stem()
          stemmer.getCurrent
        }
      } catch {
        case e: Exception => originStr
      }
  }

  override protected def validateInputType(inputType: DataType): Unit = {
    require(inputType.sameType(ArrayType(StringType)), s"Input type must be string type but got $inputType.")
  }

  override protected def outputDataType: DataType = new ArrayType(StringType, true)

  override def copy(extra: ParamMap): Stemmer = defaultCopy(extra)
}
