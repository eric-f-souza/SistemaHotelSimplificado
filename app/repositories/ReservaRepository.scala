package repositories

import models.{Reserva, ReservaTable}
import services.DatabaseService
import slick.jdbc.PostgresProfile.api.*
import slick.lifted.TableQuery

import java.time.LocalDateTime
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ReservaRepository @Inject()(db: DatabaseService)(implicit ex: ExecutionContext) {
  private val reservas = TableQuery[ReservaTable]

  def createTable(): Future[Unit] = {
    db.run(reservas.schema.createIfNotExists).map(_ => ())
  }

  def deletarReserva(id: Long): Future[Int] = {
    db.run(reservas.filter(_.id === id).delete)
  }

  def criarReserva(novaReserva: Reserva): Future[Reserva] = {
    println("Chegou no Repository")
    db.run((reservas returning reservas.map(_.id) into ((reserva, idBanco) => reserva.copy(id = Some(idBanco)))) += novaReserva)
  }

  def buscarReservas(): Future[Seq[Reserva]] = {
    db.run(reservas.result)
  }

  def buscarReservaPorId(id: Long): Future[Option[Reserva]] = {
    db.run(reservas.filter(_.id === id).result.headOption)
  }
  
  def possuiReservaPorQuartoId(idHospede: Long): Future[Seq[Reserva]] = {
    db.run(reservas.filter(_.hospedeId === idHospede).result)
  } 

  def atualizarReserva(reservaAtualizada: Reserva): Future[Int] = {
    db.run(reservas.filter(_.id === reservaAtualizada.id).update(reservaAtualizada))
  }

  def buscarPossuiReservaDentroPeriodoPorIdQuarto(idQuarto: Long, dataEntrada: LocalDateTime, dataSaida: LocalDateTime): Future[Boolean] = {
    val query = reservas.filter(
      r => r.quartoId === idQuarto
        && r.dataSaida > dataEntrada
        && r.dataEntrada < dataSaida).exists

    db.run(query.result)
  }
  
  def buscarReservaPorHospedeID(id: Long): Future[Seq[Reserva]] = {
    db.run(reservas.filter(_.hospedeId === id).result)
  } 
}
