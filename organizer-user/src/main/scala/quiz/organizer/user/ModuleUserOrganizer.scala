package quiz.organizer.user

import cats.data.NonEmptyList
import org.http4s.HttpService
import quiz.algebra.user.ModuleUserAsync
import quiz.effects.Async
import quiz.organizer.user.rest.UserRestOrganizer
import cats.implicits._

/**
  * @author Denis-Eusebiu Lazar eusebiu.lazar@busymachines.com
  * @since 19/01/19
  *
  */

trait ModuleUserOrganizer[F[_]] {this: ModuleUserAsync[F] =>

  implicit def async: Async[F]

  def userRestOrganizer: UserRestOrganizer[F] = _userRestOrganizer

  def userModuleService: HttpService[F] = _service

  private lazy val _userRestOrganizer: UserRestOrganizer[F] = new UserRestOrganizer[F](
    userAlgebra  = userAlgebra,
  )

  private lazy val _service: HttpService[F] = {
    NonEmptyList
      .of[HttpService[F]](
      userRestOrganizer.service
    )
      .reduceK
  }

}
