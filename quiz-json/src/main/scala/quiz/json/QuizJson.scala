package quiz.json

import quiz.core.PhantomType
import shapeless.tag.@@
import cats.syntax.contravariant._
import io.circe.syntax._

/**
  * @author Denis-Eusebiu Lazar eusebiu.lazar@busymachines.com
  * @since 19/01/19
  *
  */

trait QuizJson {

  final def phantomCodec[P, T <: PhantomType[P]](implicit enc: Encoder[P], dec: Decoder[P]): Codec[P @@ T] = Codec.instance[P @@ T](
    encode = Encoder.apply[P].narrow,
    decode = Decoder.apply[P].map(shapeless.tag[T](_))
  )


}
