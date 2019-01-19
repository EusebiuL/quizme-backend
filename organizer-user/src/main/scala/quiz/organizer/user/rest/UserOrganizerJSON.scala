package quiz.organizer.user.rest

import quiz.algebra.user.UserID
import quiz.json.{Codec, QuizJson}

/**
  * @author Denis-Eusebiu Lazar eusebiu.lazar@busymachines.com
  * @since 19/01/19
  *
  */

trait UserOrganizerJSON extends QuizJson{

  implicit val userIdCodec: Codec[UserID] = phantomCodec[Long, UserID.Phantom]

}
