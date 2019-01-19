package quiz.algebra

import quiz.core.PhantomType

/**
  * @author Denis-Eusebiu Lazar eusebiu.lazar@busymachines.com
  * @since 19/01/19
  *
  */

package object user {

  object UserID extends PhantomType[Long]
  type UserID = UserID.Type

  type UserModuleAlgebra[F[_]] = UserAlgebra[F]
}
