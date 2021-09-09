package jacob.weather
package model

import io.circe.*
import io.circe.generic.semiauto.*
import scala.util.*
import sttp.tapir.Schema

enum Temperature:
  case Hot, Cold, Moderate

object Temperature:
  def fromFahrenheit(temp: Double): Temperature =
    if (temp > 90) Temperature.Hot
    else if (temp < 50) Temperature.Cold
    else Temperature.Moderate

  given Decoder[Temperature] = Decoder.decodeString.emapTry(s => Try(Temperature.valueOf(s)))
  given Encoder[Temperature] = Encoder.encodeString.contramap(_.toString)
  given Schema[Temperature] = Schema.derived // This is a poor schema.
