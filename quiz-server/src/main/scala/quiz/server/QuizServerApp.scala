package quiz.server

import busymachines.effects.Scheduler
import cats.effect._
import org.http4s.HttpRoutes
import org.http4s.server.blaze.BlazeBuilder
import cats.implicits._
import fs2.Stream

/**
  * @author Denis-Eusebiu Lazar eusebiu.lazar@busymachines.com
  * @since 27/11/18
  *
  */
object QuizServerApp extends IOApp {

  implicit val scheduler: Scheduler = Scheduler.global

  override def run(args: List[String]): IO[ExitCode] = {
    for {
      server <- QuizServer.concurrent[IO]
      (serverConfig, quizModule) <- server.init
      exitCode: ExitCode <- serverStream[IO](
        config = serverConfig,
        service = quizModule.quizServerService).compile.lastOrError
    } yield exitCode
  }

  private def serverStream[F[_]: ConcurrentEffect: Timer](
      config: QuizServerConfig,
      service: HttpRoutes[F]): Stream[F, ExitCode] =
    BlazeBuilder[F]
      .bindHttp(config.port, config.host)
      .mountService(service, config.apiRoot)
      .serve

}
