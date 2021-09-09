package jacob.weather
package model

import cats.*
import cats.implicits.*
import java.text.DecimalFormat
import sttp.tapir.*
import sttp.tapir.Codec.PlainCodec
import scala.util.*

// A Longitude coordinate limited to 3 decimal places.
final case class Longitude private (milliDegrees: Int):
  def isEast: Boolean = milliDegrees >= 0
  def isWest: Boolean = ! isEast

  def simpleString: String =
    f"${milliDegrees.toDouble / 1000}%.3f"

  def fancyString: String =
    val decimal = new DecimalFormat("#.###").format(math.abs(milliDegrees.toDouble / 1000))
    val eastOrWest = if (isEast) " E" else " W"
    s"$decimal°$eastOrWest"


object Longitude:
  def validated(milliDegrees: Int): Option[Longitude] =
    if (milliDegrees <= maxMilliDegrees && milliDegrees >= minMilliDegrees) Longitude(milliDegrees).some
    else None

  def fromDouble(degrees: Double): Either[String, Longitude] =
    validated((degrees * 1000).toInt)
      .toRight(s"$degrees is not a valid Longitude, it should be between ${AntiMeridianWest.simpleString} and ${AntiMeridianEast.simpleString}")

  def decode(s: String): DecodeResult[Longitude] =
    DecodeResult.fromEitherString(
      s,
      Try(s.toDouble)
        .toEither
        .left.map(_.toString)
        .flatMap(fromDouble)
    )

  val maxMilliDegrees: Int = 180_000
  val minMilliDegrees: Int = -180_000

  val AustinTx: Longitude = Longitude(-97_733)
  val AntiMeridianEast: Longitude = Longitude(maxMilliDegrees)
  val AntiMeridianWest: Longitude = Longitude(minMilliDegrees)
  val PrimeMeridian: Longitude = Longitude(0)

  given Show[Longitude] with
    def show(lon: Longitude): String = lon.fancyString

  given PlainCodec[Longitude] = Codec.string.mapDecode(decode)(_.simpleString)
