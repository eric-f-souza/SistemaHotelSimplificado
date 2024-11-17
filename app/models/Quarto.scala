package models

import dto.{CreateQuartoDto, ReadQuartoDto, UpdateQuartoDto}
import play.api.libs.json.*
import slick.jdbc.PostgresProfile.api.*

case class Quarto(id: Option[Long], numero: String, tipo: String)

object Quarto {
  implicit val quartoFormat: Format[Quarto] = Json.format[Quarto]

  def fromReadQuartoDto(readQuartoDto: ReadQuartoDto): Quarto = {
    Quarto(Some(readQuartoDto.id), readQuartoDto.numero, readQuartoDto.tipo)
  }
  
  def fromCreateQuartoDto(createQuartoDto: CreateQuartoDto): Quarto = {
    Quarto(None, createQuartoDto.numero, createQuartoDto.tipo)
  }
  
  def fromUpdateQuartoDto(updateQuartoDto: UpdateQuartoDto): Quarto = {
    Quarto(Some(updateQuartoDto.id), updateQuartoDto.numero, updateQuartoDto.tipo)
  }
  
  def apply(id: Option[Long], numero: String, tipo: String): Quarto = new Quarto(id, numero, tipo)
}

class QuartoTable(tag: Tag) extends Table[Quarto](tag, "quartos") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def numero = column[String]("numero")
  def tipo = column[String]("tipo")

  def * = (id.?, numero, tipo) <> ((Quarto.apply _).tupled, Quarto.unapply)
}