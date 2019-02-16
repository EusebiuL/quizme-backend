package quiz.db.config

import cats.effect.Async
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.{Completed, MongoClient, MongoDatabase, Observable, Observer}

/**
  * @author Denis-Eusebiu Lazar eusebiu.lazar@busymachines.com
  * @since 29/01/19
  *
  */
object DatabaseConfigAlgebra {
  def initialiseDb[F[_]: Async](
      config: DatabaseConfig): F[MongoDatabase] = Async[F].delay {
    val client: MongoClient =
      MongoClient(s"mongodb://${config.user}:${config.password}@${config.host}:${config.port}/${config.name}")
    val db = client.getDatabase(config.name)

    db.drop().subscribe(new Observer[Completed]{
      override def onNext(result: Completed): Unit = println(s"onNext: $result")
      override def onError(e: Throwable): Unit = println(s"onError: $e")
      override def onComplete(): Unit = println("onComplete")
    })
    db
  }
}
