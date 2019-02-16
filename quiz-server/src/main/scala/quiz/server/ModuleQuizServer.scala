package quiz.server

import cats.data.NonEmptyList
import org.http4s.HttpRoutes
import quiz.algebra.user.ModuleUserAsync
import quiz.effects.Async
import quiz.organizer.user.ModuleUserOrganizer
import cats.implicits._
import org.mongodb.scala.{Document, MongoCollection, MongoDatabase}

/**
  * @author Denis-Eusebiu Lazar eusebiu.lazar@busymachines.com
  * @since 27/11/18
  *
  */
trait ModuleQuizServer[F[_]] extends ModuleUserOrganizer[F] with ModuleUserAsync[F]{

  implicit override def async: Async[F] = async

  override def database: MongoDatabase


  def quizServerService: HttpRoutes[F] =  {
    import cats.implicits._
    val service: HttpRoutes[F] = {
      NonEmptyList
        .of[HttpRoutes[F]](
          userModuleService
      )
        .reduceK
    }
    service

  }
}

object ModuleQuizServer {
  def concurrent[F[_]](db: MongoDatabase)(implicit a: Async[F]): ModuleQuizServer[F] = new ModuleQuizServer[F]{
    implicit override def async: Async[F] = a

    override def database: MongoDatabase = db
  }
}
