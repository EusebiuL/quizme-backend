package quiz

import busymachines.{json => bmj}

/**
  * @author Denis-Eusebiu Lazar eusebiu.lazar@busymachines.com
  * @since 19/01/19
  *
  */

package object json extends bmj.JsonTypeDefinitions with bmj.DefaultTypeDiscriminatorConfig {
  type Codec[A] = bmj.Codec[A]
  @inline def Codec: bmj.Codec.type = bmj.Codec
}
