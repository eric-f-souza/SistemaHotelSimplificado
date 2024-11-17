package dto

import models.Quarto
import play.api.libs.json.{Format, Json}

case class ReadQuartoDto (id: Long, numero: String, tipo: String)

object ReadQuartoDto {
  implicit val readQuartoDtoFormat: Format[ReadQuartoDto] = Json.format[ReadQuartoDto]

  def fromQuarto(quarto: Quarto): ReadQuartoDto = {
    ReadQuartoDto(quarto.id.get, quarto.numero, quarto.tipo)
  }

  def fromQuartos(quartos: Seq[Quarto]): Seq[ReadQuartoDto] = {
    quartos.map(fromQuarto)
  }
}
