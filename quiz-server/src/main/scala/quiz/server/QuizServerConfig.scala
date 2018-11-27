package quiz.server

import cats.effect.Sync
import quiz.config.ConfigLoader

/**
  * @author Denis-Eusebiu Lazar eusebiu.lazar@busymachines.com
  * @since 27/11/18
  *
  */
final case class QuizServerConfig(
    port: Int,
    host: String,
    apiRoot: String
)

object QuizServerConfig extends ConfigLoader[QuizServerConfig] {
  override def default[F[_]: Sync]: F[QuizServerConfig] = this.load[F]("quiz.server")
}