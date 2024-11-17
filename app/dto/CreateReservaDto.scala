package dto

import models.Reserva
import play.api.libs.json.{Format, Json}

import java.time.LocalDateTime

case class CreateReservaDto(quartoId: Long, hospedeId: Long, dataEntrada: LocalDateTime, dataSaida: LocalDateTime)

object CreateReservaDto {
  implicit val createReservaDtoFormat: Format[CreateReservaDto] = Json.format[CreateReservaDto]
}
