package dto

import models.Hospede
import play.api.libs.json.{Format, Json}

case class ReadHospedeDto(id: Long, nome: String, cpf: String, email: String, telefone: String)

object ReadHospedeDto {
  implicit val readHospedeDtoFormat: Format[ReadHospedeDto] = Json.format[ReadHospedeDto]

  def fromHospede(hospede: Hospede): ReadHospedeDto =  {
    ReadHospedeDto(hospede.id.get, hospede.nome, hospede.cpf, hospede.email, hospede.telefone)
  }
  
  def fromHospedes(hospedes: Seq[Hospede]): Seq[ReadHospedeDto] = hospedes.map(fromHospede)
  
  def aplly(id: Long, nome: String, cpf: String, email: String, telefone: String): ReadHospedeDto = {
    ReadHospedeDto(id, nome, cpf, email, telefone)
  }
}
