package quiz.json

import busymachines.json.{JsonSyntax, SemiAutoDerivation}

/**
  * @author Denis-Eusebiu Lazar eusebiu.lazar@busymachines.com
  * @since 19/01/19
  *
  */

object syntax extends JsonSyntax.Implicits

object derive extends SemiAutoDerivation

object autoderive extends io.circe.generic.extras.AutoDerivation
