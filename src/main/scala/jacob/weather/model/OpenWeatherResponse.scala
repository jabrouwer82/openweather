package jacob.weather
package model

import io.circe.*
import io.circe.generic.semiauto.*
import sttp.tapir.Schema

final case class OpenWeatherResponse(
  weather: String,
  temperature: Double,
  alerts: List[Alert],
)

object OpenWeatherResponse:
  given Decoder[OpenWeatherResponse] = new Decoder[OpenWeatherResponse]:
    final def apply(c: HCursor): Decoder.Result[OpenWeatherResponse] =
      for {
        weather <- c.downField("current").downField("weather").downArray.downField("description").as[String]
        temperature <- c.downField("current").downField("temp").as[Double]
        alerts <- c.downField("alerts").as[Option[List[Alert]]]
      } yield OpenWeatherResponse(weather, temperature, alerts.getOrElse(Nil))

  given Encoder[OpenWeatherResponse] = deriveEncoder // This isn't valid, nor is it used.
  given Schema[OpenWeatherResponse] = Schema.derived
