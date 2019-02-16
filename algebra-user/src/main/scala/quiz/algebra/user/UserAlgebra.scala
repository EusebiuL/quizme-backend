package quiz.algebra.user

import org.mongodb.scala.MongoDatabase

/**
  * @author Denis-Eusebiu Lazar eusebiu.lazar@busymachines.com
  * @since 19/01/19
  *
  */

trait UserAlgebra[F[_]] {

  def findUserById(userId: UserID): F[UserID]

}

object UserAlgebra {
  import quiz.effects._

  def async[F[_]: Async](database: MongoDatabase): UserAlgebra[F] = new impl.AsyncAlgebraImpl[F](database)
}