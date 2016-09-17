package org.apache.spark.ml.feature

import org.scalatest.FunSuite
import org.apache.spark.ml.feature.{ RegexTokenizer, StopWordsRemover }

class StemmerSuite extends FunSuite with LocalSparkContext {
  test("Stemming of Freanch words") {

    val data = sqlContext.createDataFrame(Seq(
      ("Trouvez votre Voiture d'occasion parmi des milliers de petites", 1),
      ("Découvrez gratuitement tous les articles", 2),
      ("majorité", 3),
      ("contractés", 4),
      ("contradictoirement", 5))).toDF("word", "id")

    val tokenizer = new RegexTokenizer()
      .setInputCol("word")
      .setPattern("[a-z0-9éèêâîûùäüïëô]+")
      .setGaps(false)
      .setOutputCol("rawTokens")

    val stemmed = new Stemmer()
      .setInputCol("rawTokens")
      .setLanguage("French")
      .setOutputCol("stemmed")

    stemmed.transform(tokenizer.transform(data)).show

  }

  test("Stemming of non-English words") {
    val data = sqlContext.createDataFrame(Seq(
      ("övrigt", 1),
      ("bildelar", 2),
      ("biltillbehör", 3))).toDF("word", "id")

    //    val stemmed = new Stemmer()
    //      .setInputCol("word")
    //      .setOutputCol("stemmed")
    //      .setLanguage("Swedish")
    //      .transform(data.select("word")).show

    //    val expected = sqlContext.createDataFrame(Seq(
    //      ("övrigt", 1, "övr"),
    //      ("bildelar", 2, "bildel"),
    //      ("biltillbehör", 3, "biltillbehör"))).toDF("word", "id", "stemmed")
    //
    //    assert(stemmed.collect().deep == expected.collect().deep)
  }

}
