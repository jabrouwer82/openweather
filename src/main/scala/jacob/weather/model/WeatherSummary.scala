package jacob.weather
package model

import io.circe.*
import sttp.tapir.Schema

final case class WeatherSummary(
  condition: String,
  temperature: Temperature,
  alerts: List[Alert],
)

object WeatherSummary:
  val description =
    "A summary of the weather report, including current precipitation and atmospheric conditions, an extremely coarse temperature, and any active alerts"
  val example = WeatherSummary("freezing rain", Temperature.Hot, List(Alert.example))

  def fromOpenWeatherResponse(owr: OpenWeatherResponse): WeatherSummary =
    WeatherSummary(owr.weather, Temperature.fromFahrenheit(owr.temperature), owr.alerts)

  given Codec[WeatherSummary] = Codec
    .forProduct3("condition", "temperature", "alerts")(WeatherSummary.apply)(ws => (ws.condition, ws.temperature, ws.alerts))
  given Schema[WeatherSummary] = Schema.derived
