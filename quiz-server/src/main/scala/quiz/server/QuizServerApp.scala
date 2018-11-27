package quiz.server

import busymachines.effects.Scheduler
import cats.effect.{ConcurrentEffect, IO}
import fs2.{Stream, StreamApp}
import org.http4s.HttpService
import org.http4s.server.blaze.BlazeBuilder
import cats.implicits._

/**
  * @author Denis-Eusebiu Lazar eusebiu.lazar@busymachines.com
  * @since 27/11/18
  *
  */

object QuizServerApp extends StreamApp[IO]{

  implicit val scheduler: Scheduler = Scheduler.global

  override def stream(args: List[String], requestShutdown: IO[Unit]): Stream[IO, StreamApp.ExitCode] = {
    for {

      server <- QuizServer.concurrent[IO]
      (serverConfig, quizModule) <- server.init
      exitCode <- serverStream[IO](config = serverConfig/*, service = quizModule.quizServerService*/)
    } yield exitCode
  }


  private def serverStream[F[_]: ConcurrentEffect](config: QuizServerConfig/*, service: HttpService[F]*/): Stream[F, StreamApp.ExitCode] =
    BlazeBuilder[F]
    .bindHttp(config.port, config.host)
//    .mountService(service, config.apiRoot)
    .serve


}
