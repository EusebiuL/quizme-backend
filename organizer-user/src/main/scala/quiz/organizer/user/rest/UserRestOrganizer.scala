package quiz.organizer.user.rest

import cats.data.NonEmptyList
import cats.effect.Async
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import quiz.algebra.user.{UserAlgebra, UserID}
import cats.implicits._
import quiz.http._

/**
  * @author Denis-Eusebiu Lazar eusebiu.lazar@busymachines.com
  * @since 19/01/19
  *
  */
final class UserRestOrganizer[F[_]](
    private val userAlgebra: UserAlgebra[F]
) (implicit val F: Async[F]) extends Http4sDsl[F] with UserOrganizerJSON{

  private val userRestOrganizer: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / "user" / LongVar(id) =>
      for{
        user <- F.pure(UserID(id))
        resp <- Ok(user)
      } yield resp
  }


  val service: HttpRoutes[F] = {
    import cats.implicits._
    NonEmptyList
      .of(
        userRestOrganizer
      )
      .reduceK
  }

}
