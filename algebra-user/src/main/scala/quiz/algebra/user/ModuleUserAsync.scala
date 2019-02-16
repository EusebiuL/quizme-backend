package quiz.algebra.user

import org.mongodb.scala.{Document, MongoCollection, MongoDatabase}
import quiz.effects.Async

/**
  * @author Denis-Eusebiu Lazar eusebiu.lazar@busymachines.com
  * @since 19/01/19
  *
  */
trait ModuleUserAsync[F[_]] {

  implicit def async: Async[F]

  def database: MongoDatabase

  def userAlgebra: UserAlgebra[F] = _moduleAlgebra

  private lazy val _moduleAlgebra: UserModuleAlgebra[F] =
    new impl.AsyncAlgebraImpl[F](database)

}
