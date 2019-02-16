package quiz.algebra.user.impl

import org.mongodb.scala.MongoDatabase
import quiz.algebra.user.{UserAlgebra, UserID}
import quiz.effects.Async

/**
  * @author Denis-Eusebiu Lazar eusebiu.lazar@busymachines.com
  * @since 19/01/19
  *
  */
final private[user] class AsyncAlgebraImpl[F[_]](database: MongoDatabase)(
    implicit val F: Async[F]
) extends UserAlgebra[F] {

  override def findUserById(userId: UserID): F[UserID] = F.pure(UserID(1))

}
