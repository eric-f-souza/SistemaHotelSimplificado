package dto

import models.Reserva
import play.api.libs.json.{Format, Json}

import java.time.LocalDateTime

case class UpdateReservaDto(id: Long, quartoId: Long, hospedeId: Long, dataEntrada: LocalDateTime, dataSaida: LocalDateTime)

object UpdateReservaDto {
  implicit val updateReservaDtoFormat: Format[UpdateReservaDto] = Json.format[UpdateReservaDto]
}
