package org.apache.spark.ml.feature

import org.scalatest.Suite
import org.scalatest.BeforeAndAfterAll

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SQLContext

trait LocalSparkContext extends BeforeAndAfterAll { self: Suite =>
  @transient var sc: SparkContext = _
  @transient var sqlContext: SQLContext = _

  override def beforeAll() {
    val conf = new SparkConf()
      .setMaster("local")
      .setAppName("test")
    sc = new SparkContext(conf)
    sqlContext = new SQLContext(sc)
    super.beforeAll()
  }

  override def afterAll() {
    if (sc != null) {
      sc.stop()
    }
    super.afterAll()
  }
}
