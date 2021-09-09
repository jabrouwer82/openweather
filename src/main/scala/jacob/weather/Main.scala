package jacob.weather

import cats.*
import cats.effect.*
import cats.effect.implicits.*
import cats.implicits.*
import jacob.weather.http.*
import org.http4s.*
import org.http4s.HttpRoutes
import org.http4s.blaze.server.*
import org.http4s.dsl.io._
import org.http4s.implicits.*
import scala.concurrent.ExecutionContext.global
import sttp.tapir.docs.openapi.*
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.openapi.OpenAPI
import sttp.tapir.openapi.circe.yaml.*
import sttp.tapir.swagger.*

object Main extends IOApp:
  def run(args: List[String]): IO[ExitCode] =
    for {
      openWeatherApiKey <- IO(sys.env("OPEN_WEATHER_API_KEY"))
      owc <- OpenWeatherClient.build(openWeatherApiKey)
      summaryService <- IO(WeatherSummaryService(owc))

      server <- BlazeServerBuilder[IO](global)
        .bindHttp(8282, "localhost")
        .withHttpApp(
          List(
            summaryService.summary,
            Swagger.swagger,
          ).reduceLeft(_ <+> _).orNotFound
        )
        .serve
        .compile
        .drain
        .as(ExitCode.Success)
    } yield server
