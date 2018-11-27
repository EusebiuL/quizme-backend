package quiz.server

import cats.data.NonEmptyList
import org.http4s.HttpService
import org.http4s.dsl.Http4sDsl

/**
  * @author Denis-Eusebiu Lazar eusebiu.lazar@busymachines.com
  * @since 27/11/18
  *
  */
trait ModuleQuizServer[F[_]] {

//  def quizServerService: HttpService[F] = HttpService.empty
//  {
//    import cats.implicits._
//    val service: HttpService[F] = {
//      NonEmptyList
//        .of[HttpService[F]]()
//        .reduceK
//    }

  }

object ModuleQuizServer {
  def concurrent[F[_]](): ModuleQuizServer[F] = new ModuleQuizServer[F]{}
}
