package quiz.server

import cats.effect.{Concurrent, ConcurrentEffect}
import fs2.Stream
import busymachines.effects.Scheduler

/**
  * @author Denis-Eusebiu Lazar eusebiu.lazar@busymachines.com
  * @since 27/11/18
  *
  */
final class QuizServer[F[_]: ConcurrentEffect] private (
    implicit val scheduler: Scheduler
) {

  def init: Stream[F, (QuizServerConfig, ModuleQuizServer[F])] = {
    for {
      serverConfig <- Stream.eval(QuizServerConfig.default[F])
      quizModule <- Stream.eval(moduleInit())
    } yield (serverConfig, quizModule)
  }

  private def moduleInit(): F[ModuleQuizServer[F]] =
    Concurrent
      .apply[F]
      .delay(
        ModuleQuizServer
          .concurrent(implicitly)
      )
}

object QuizServer {
  def concurrent[F[_]: ConcurrentEffect](
      implicit scheduler: Scheduler
  ): Stream[F, QuizServer[F]] =
    Stream.eval(Concurrent.apply[F].delay(new QuizServer[F]()))
}
