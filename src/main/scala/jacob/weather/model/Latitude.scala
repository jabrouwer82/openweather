package jacob.weather
package model

import cats.*
import cats.implicits.*
import java.text.DecimalFormat
import sttp.tapir.*
import sttp.tapir.Codec.PlainCodec
import scala.util.*


// A Latitude coordinate limited to 3 decimal places.
final case class Latitude private (milliDegrees: Int):
  def isNorth: Boolean = milliDegrees >= 0
  def isSouth: Boolean = ! isSouth

  def simpleString: String =
    f"${milliDegrees.toDouble / 1000}%.3f"

  def fancyString: String =
    val decimal = new DecimalFormat("#.###").format(math.abs(milliDegrees.toDouble / 1000))
    val northOrSouth = if (isNorth) " N" else " S"
    s"$decimal°$northOrSouth"

object Latitude:
  def validated(milliDegrees: Int): Option[Latitude] =
    if (milliDegrees <= maxMilliDegrees && milliDegrees >= minMilliDegrees) Latitude(milliDegrees).some
    else None

  def fromDouble(degrees: Double): Either[String, Latitude] =
    validated((degrees * 1000).toInt)
      .toRight(s"$degrees is not a valid Latitude, it should be between ${NorthPole.simpleString} and ${SouthPole.simpleString}")

  def decode(s: String): DecodeResult[Latitude] =
    DecodeResult.fromEitherString(
      s,
      Try(s.toDouble)
        .toEither
        .left.map(_.toString)
        .flatMap(fromDouble)
    )

  val maxMilliDegrees: Int = 90_000
  val minMilliDegrees: Int = -90_000

  val AustinTx: Latitude = Latitude(30_266)
  val NorthPole: Latitude = Latitude(maxMilliDegrees)
  val SouthPole: Latitude = Latitude(minMilliDegrees)
  val Equator: Latitude = Latitude(0)

  given Show[Latitude] with
    def show(lat: Latitude): String = lat.fancyString

  given PlainCodec[Latitude] = Codec.string.mapDecode(decode)(_.simpleString)
