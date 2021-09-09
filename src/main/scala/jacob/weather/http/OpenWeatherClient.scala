package jacob.weather
package http

import jacob.weather.model.*
import cats.effect.*
import sttp.tapir.client.http4s.Http4sClientInterpreter
import org.http4s.blaze.client.*
import org.http4s.client.*
import scala.concurrent.ExecutionContext.global
import sttp.model.*

final case class OpenWeatherClient(apiKey: String, client: Resource[IO, Client[IO]]):
  val excludes = "minutely,hourly,daily"
  val units = "imperial"

  val onecallClient = Http4sClientInterpreter[IO]().toRequestUnsafe(OpenWeatherApi.onecall, baseUri = Some("https://api.openweathermap.org"))
  def onecall(coords: Coordinates): IO[Either[StatusCode, OpenWeatherResponse]] =
    val (req, responseParser) = onecallClient((coords.latitude, coords.longitude, apiKey, excludes, units))
    client.use(_.run(req).use(responseParser))

object OpenWeatherClient:
  def build(apiKey: String): IO[OpenWeatherClient] =
    IO(OpenWeatherClient(apiKey, BlazeClientBuilder[IO](global).resource))
