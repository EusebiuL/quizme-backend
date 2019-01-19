package quiz.core

import shapeless.tag.@@

/**
  * @author Denis-Eusebiu Lazar eusebiu.lazar@busymachines.com
  * @since 19/01/19
  *
  */

trait PhantomType[T] {

  type Phantom <: this.type

  type Raw  = T
  type Type = T @@ Phantom

  @inline def apply(value: T): @@[T, Phantom] =
    shapeless.tag[Phantom](value)

  @inline def unapply(phantom: Type): T =
    identity(phantom)

}
