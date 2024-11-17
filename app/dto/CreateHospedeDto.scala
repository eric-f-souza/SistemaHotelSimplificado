package dto

import models.Hospede
import play.api.libs.json.{Format, Json}

case class CreateHospedeDto(nome: String, cpf: String, email: String, telefone: String)

object CreateHospedeDto {
  implicit val createHospedeDtoFormat: Format[CreateHospedeDto] = Json.format[CreateHospedeDto]
}
