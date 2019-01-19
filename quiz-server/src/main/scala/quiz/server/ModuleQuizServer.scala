package quiz.server

import cats.data.NonEmptyList
import org.http4s.HttpService
import org.http4s.dsl.Http4sDsl
import quiz.algebra.user.ModuleUserAsync
import quiz.effects.Async
import quiz.organizer.user.ModuleUserOrganizer
import cats.implicits._

/**
  * @author Denis-Eusebiu Lazar eusebiu.lazar@busymachines.com
  * @since 27/11/18
  *
  */
trait ModuleQuizServer[F[_]] extends ModuleUserOrganizer[F] with ModuleUserAsync[F]{

  implicit override def async: Async[F] = async


  def quizServerService: HttpService[F] =  {
    import cats.implicits._
    val service: HttpService[F] = {
      NonEmptyList
        .of[HttpService[F]](
          userModuleService
      )
        .reduceK
    }
    service

  }
}

object ModuleQuizServer {
  def concurrent[F[_]](implicit a: Async[F]): ModuleQuizServer[F] = new ModuleQuizServer[F]{
    implicit override def async: Async[F] = a
  }
}
