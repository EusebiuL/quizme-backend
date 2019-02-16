package quiz.server

import cats.effect.{Concurrent, ConcurrentEffect}
import busymachines.effects.Scheduler
import cats.implicits._
import org.mongodb.scala.MongoDatabase
import quiz.db.config.{DatabaseConfig, DatabaseConfigAlgebra}

/**
  * @author Denis-Eusebiu Lazar eusebiu.lazar@busymachines.com
  * @since 27/11/18
  *
  */
final class QuizServer[F[_]: ConcurrentEffect] private (
    implicit val scheduler: Scheduler
) {

  def init: F[(QuizServerConfig, ModuleQuizServer[F])] = {
    for {
      serverConfig <- QuizServerConfig.default[F]
      databaseConfig <- DatabaseConfig.default[F]
      database <- DatabaseConfigAlgebra.initialiseDb(databaseConfig)
      quizModule <- moduleInit(database)
    } yield (serverConfig, quizModule)
  }

  private def moduleInit(database: MongoDatabase): F[ModuleQuizServer[F]] =
    Concurrent
      .apply[F]
      .delay(
        ModuleQuizServer
          .concurrent(database)(implicitly)
      )
}

object QuizServer {
  def concurrent[F[_]: ConcurrentEffect](
      implicit scheduler: Scheduler
  ): F[QuizServer[F]] =
    Concurrent.apply[F].delay(new QuizServer[F]())
}
