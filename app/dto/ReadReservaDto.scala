package dto

import models.Reserva
import play.api.libs.json.{Format, Json}

import java.time.LocalDateTime

case class ReadReservaDto(id: Long, quartoId: Long, hospedeId: Long, dataEntrada: LocalDateTime, dataSaida: LocalDateTime)

object ReadReservaDto {
  implicit val readReservaDtoFormat: Format[ReadReservaDto] = Json.format[ReadReservaDto]

  def fromReserva(reserva: Reserva): ReadReservaDto = {
    ReadReservaDto(reserva.id.get, reserva.quartoId, reserva.hospedeId, reserva.dataEntrada, reserva.dataSaida)
  }

  def fromReservas(reservas: Seq[Reserva]): Seq[ReadReservaDto] = reservas.map(fromReserva)
}
