package models

import dto.{CreateReservaDto, UpdateReservaDto}
import play.api.libs.json.*
import slick.jdbc.PostgresProfile.api.*

import java.time.LocalDateTime

case class Reserva(id: Option[Long], quartoId: Long, hospedeId: Long, dataEntrada: LocalDateTime, dataSaida: LocalDateTime)

object Reserva {
  implicit val reservaFormat: Format[Reserva] = Json.format[Reserva]
  
  def fromCreateReservaDto(createReservaDto: CreateReservaDto): Reserva = {
    Reserva(
      None,
      createReservaDto.quartoId,
      createReservaDto.hospedeId,
      createReservaDto.dataEntrada,
      createReservaDto.dataSaida)
  }
  
  def fromUpdateReservaDto(updateReservaDto: UpdateReservaDto): Reserva = {
    Reserva(
      Some(updateReservaDto.id),
      updateReservaDto.quartoId,
      updateReservaDto.hospedeId,
      updateReservaDto.dataEntrada,
      updateReservaDto.dataSaida)
  }
}

class ReservaTable(tag: Tag) extends Table[Reserva](tag, "reservas") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def quartoId = column[Long]("quarto_id")
  def hospedeId = column[Long]("hospede_id")
  def dataEntrada = column[LocalDateTime]("data_entrada")
  def dataSaida = column[LocalDateTime]("data_saida")
  
  def * = (id.?, quartoId, hospedeId, dataEntrada, dataSaida) <> ((Reserva.apply _).tupled, Reserva.unapply)
}
