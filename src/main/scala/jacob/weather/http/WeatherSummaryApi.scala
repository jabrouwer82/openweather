package jacob.weather
package http

import sttp.tapir.*
import sttp.tapir.json.circe.*
import sttp.model.*
import io.circe.*

import jacob.weather.model.*

object WeatherSummaryApi:
  val summary: Endpoint[Coordinates, StatusCode, WeatherSummary, Any] = endpoint
    .get
    .in(
      "summary" / query[Latitude]("lat").and(query[Longitude]("lon")).mapTo[Coordinates]
    )
    .errorOut(
      statusCode
        .description(StatusCode.BadRequest, "The given latitude or longitude is invalid or malformed.")
        .description(StatusCode.InternalServerError, "Congrats, you broke it.")
    )
    .out(
      jsonBody[WeatherSummary]
        .description(WeatherSummary.description)
        .example(WeatherSummary.example)
    )
    .description(
      """Summarizes the weather at the provided latitude and longitude.
        |Includes precipitation status, temperature feel, and any weather alerts.""".stripMargin
    )
