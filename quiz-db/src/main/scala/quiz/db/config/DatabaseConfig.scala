package quiz.db.config

import cats.effect.Sync
import quiz.config.ConfigLoader

/**
  * @author Denis-Eusebiu Lazar eusebiu.lazar@busymachines.com
  * @since 29/01/19
  *
  */
final case class DatabaseConfig(
    name: String,
    host: String,
    user: String,
    password: String,
    port: String,
)

object DatabaseConfig extends ConfigLoader[DatabaseConfig] {
  override def default[F[_]: Sync]: F[DatabaseConfig] = this.load[F]("quiz.db")
}
