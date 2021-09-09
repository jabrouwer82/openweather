package jacob.weather
package model

import io.circe.*
import sttp.tapir.Schema

final case class Alert(
  sender: String,
  event: String,
  start: Int,
  end: Int,
  description: String,
  tags: List[String],
)

object Alert:
  val example: Alert = Alert(
    "NWS Tulsa",
    "Heat Advisory",
    1597341600,
    1597366800,
    "...HEAT ADVISORY REMAINS IN EFFECT FROM 1 PM THIS AFTERNOON TO\n8 PM CDT THIS EVENING...\n* WHAT...Heat index values of 105 to 109 degrees expected.\n* WHERE...Creek, Okfuskee, Okmulgee, McIntosh, Pittsburg,\nLatimer, Pushmataha, and Choctaw Counties.\n* WHEN...From 1 PM to 8 PM CDT Thursday.\n* IMPACTS...The combination of hot temperatures and high\nhumidity will combine to create a dangerous situation in which\nheat illnesses are possible.",
    List("Extreme temperature value")
  )

  given Codec[Alert] = Codec.forProduct6("sender_name", "event", "start", "end", "description", "tags")(Alert.apply)(a => (a.sender, a.event, a.start, a.end, a.description, a.tags))
  given Schema[Alert] = Schema.derived
