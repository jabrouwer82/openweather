package jacob.weather
package http

import sttp.tapir.*
import sttp.tapir.json.circe.*
import sttp.model.*
import io.circe.*

import jacob.weather.model.*

object OpenWeatherApi:

  val onecall: Endpoint[(Latitude, Longitude, String, String, String), StatusCode, OpenWeatherResponse, Any] = endpoint
    .get
    .in(
      "data" / "2.5" / "onecall" /
        query[Latitude]("lat")
          .and(query[Longitude]("lon"))
          .and(query[String]("appid"))
          .and(query[String]("exclude"))
          .and(query[String]("units"))
    )
    .errorOut(statusCode)
    .out(
      jsonBody[OpenWeatherResponse]
    )
