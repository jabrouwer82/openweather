package jacob.weather
package model

final case class Coordinates(
  latitude: Latitude,
  longitude: Longitude,
)

object Coordinates:
  val AustinTx: Coordinates = Coordinates(Latitude.AustinTx, Longitude.AustinTx)
  val NullIsland: Coordinates = Coordinates(Latitude.Equator, Longitude.PrimeMeridian)
