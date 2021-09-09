package jacob.weather
package http

import cats.*
import cats.effect.*
import cats.effect.implicits.*
import cats.implicits.*
import jacob.weather.model.*
import org.http4s.*
import scala.util.*
import sttp.model.*
import sttp.tapir.server.http4s.*

final case class WeatherSummaryService(openWeatherClient: OpenWeatherClient):
  val summary: HttpRoutes[IO] =
    Http4sServerInterpreter[IO]().toRoutes(WeatherSummaryApi.summary)(coords =>
      openWeatherClient.onecall(coords).map(_.map(WeatherSummary.fromOpenWeatherResponse))
    )


