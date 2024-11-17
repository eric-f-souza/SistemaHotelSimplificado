package dto

import models.Quarto
import play.api.libs.json.{Format, Json}

case class UpdateQuartoDto(id: Long, numero: String, tipo: String)

object UpdateQuartoDto {
  implicit val updateQuartoDtoFormat: Format[UpdateQuartoDto] = Json.format[UpdateQuartoDto]
}
