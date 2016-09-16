# Spark Stemming

[![Build Status](https://travis-ci.org/master/spark-stemming.svg?branch=master)](https://travis-ci.org/master/spark-stemming)

[Snowball](http://snowballstem.org/) is a small string processing language
designed for creating stemming algorithms for use in Information Retrieval.
This package allows to use it as a part of [Spark ML
Pipeline](https://spark.apache.org/docs/latest/ml-guide.html) API.

## Linking

Link against this library using SBT:

```
libraryDependencies += "master" %% "spark-stemming" % "0.1.2"
```

Using Maven:

```xml
<dependency>
    <groupId>master</groupId>
    <artifactId>spark-stemming_2.10</artifactId>
    <version>0.1.2</version>
</dependency>
```

Or include it when starting the Spark shell:

```
$ bin/spark-shell --packages master:spark-stemming_2.10:0.1.2
```

## Features

Currently implemented algorithms:

* English
* English (Porter)
* Romance stemmers:
 * French
 * Spanish
 * Portuguese
 * Italian
 * Romanian
* Germanic stemmers:
 * German
 * Dutch
* Scandinavian stemmers:
 * Swedish
 * Norwegian (Bokmål)
 * Danish
* Russian
* Finnish
* Greek

More details are on the [Snowball stemming algorithms](http://snowballstem.org/algorithms/) page.

## Usage

`Stemmer`
[Transformer](https://spark.apache.org/docs/latest/ml-guide.html#transformers)
can be used directly or as a part of ML
[Pipeline](https://spark.apache.org/docs/latest/ml-guide.html#pipeline). In
particular, it is nicely combined with
[Tokenizer](https://spark.apache.org/docs/latest/ml-features.html#tokenizer).

```scala
import org.apache.spark.ml.feature.{ CountVectorizer, CountVectorizerModel, RegexTokenizer, StopWordsRemover,Stemmer }

val data = sqlContext
  .createDataFrame(Seq(("coucou", 1), ("la voiture", 2), ("la chance", 3)))
  .toDF("word", "id")

 // Pipeline stages
    val tokenizer = new RegexTokenizer()
      .setInputCol("word")
      .setPattern("[a-z0-9éèêâîûùäüïëô]+")
      .setGaps(false)
      .setOutputCol("rawTokens")

    val stopWordsRemover = new StopWordsRemover()
      .setInputCol("rawTokens")
      .setOutputCol("tokens")
    stopWordsRemover.setStopWords(stopWordsRemover.getStopWords ++ stopWordText)

    val stemmer = new Stemmer()
      .setInputCol("tokens")
      .setOutputCol("stemmed")
      .setLanguage("French")
      
stemmer.transform(stopWordsRemover.transform(tokenizer.transform(data.select("word")))).show
```
