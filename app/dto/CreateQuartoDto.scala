package dto

import play.api.libs.json.{Format, Json}

case class CreateQuartoDto(numero: String, tipo: String)

object CreateQuartoDto {
  implicit val quartoFormat: Format[CreateQuartoDto] = Json.format[CreateQuartoDto]
}
