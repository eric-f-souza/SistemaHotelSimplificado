package models

import dto.{CreateHospedeDto, UpdateHospedeDto}
import play.api.libs.json.*
import slick.jdbc.PostgresProfile.api.*

case class Hospede(id: Option[Long], nome: String, cpf: String, email: String, telefone: String)

object Hospede{
  implicit val hospedeFormat: Format[Hospede] = Json.format[Hospede]
  
  def fromCreateHospedeDto(createHospedeDto: CreateHospedeDto): Hospede = {
    Hospede(None, createHospedeDto.nome, createHospedeDto.cpf, createHospedeDto.email, createHospedeDto.telefone)
  }
  
  def fromUpdateHospedeDto(updateHospedeDto: UpdateHospedeDto): Hospede = {
    Hospede(
      Some(updateHospedeDto.id),
      updateHospedeDto.nome,
      updateHospedeDto.cpf,
      updateHospedeDto.email,
      updateHospedeDto.telefone
    )
  }

  def aplly(id: Option[Long], nome: String, cpf: String, email: String, telefone: String): Hospede = {
    new Hospede(id, nome, cpf, email, telefone )
  }
}

class HospedeTable(tag: Tag) extends Table[Hospede](tag, "hospedes") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def nome = column[String]("nome")
  def cpf = column[String]("cpf")
  def email = column[String]("email")
  def telefone = column[String]("telefone")

  def * = (id.?, nome, cpf, email, telefone) <> ((Hospede.apply _).tupled, Hospede.unapply)
}
