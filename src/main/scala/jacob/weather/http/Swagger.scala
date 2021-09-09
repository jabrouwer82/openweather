package jacob.weather
package http

import sttp.tapir.docs.openapi.*
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.openapi.OpenAPI
import sttp.tapir.openapi.circe.yaml.*
import sttp.tapir.swagger.*
import org.http4s.*
import sttp.tapir.server.http4s.*
import cats.effect.*

object Swagger:
    val docsYaml = OpenAPIDocsInterpreter().toOpenAPI(List(WeatherSummaryApi.summary), "Weather Summary", "0.1").toYaml

    val swagger: HttpRoutes[IO] =
        Http4sServerInterpreter[IO]().toRoutes(SwaggerUI[IO](docsYaml))
