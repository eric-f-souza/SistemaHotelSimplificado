package dto

import models.Hospede
import play.api.libs.json.{Format, Json}

case class UpdateHospedeDto(id: Long, nome: String, cpf: String, email: String, telefone: String)

object UpdateHospedeDto {
  implicit val updateHospedeDtoFormat: Format[UpdateHospedeDto] = Json.format[UpdateHospedeDto]
}
