package services

import cats.data.EitherT
import cats.implicits.*
import dto.{CreateReservaDto, ReadReservaDto, UpdateReservaDto}
import models.Reserva
import repositories.ReservaRepository

import java.time.LocalDateTime
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ReservaService @Inject()(reservaRepository: ReservaRepository,
                               quartoService: QuartoService,
                               hospedeService: HospedeService)(implicit ec: ExecutionContext) {
  def createTable(): Future[Unit] = {
    reservaRepository.createTable()
  }

  def criarReserva(dto: CreateReservaDto, tempoLimpesa: Int): Future[Either[ReservaError, ReadReservaDto]] = {
    val resultado = for {
      _ <- EitherT(validarQuartoCadastrado(dto.quartoId))
      _ <- EitherT(validarDisponibilidadeQuarto(dto,tempoLimpesa))
      _ <- EitherT(validarHospedeCadastrado(dto.hospedeId))
      _ <- EitherT(validarHospedePossuiReserva(dto.hospedeId))
      novaReserva <- EitherT(gravarReservaBanco(dto))
    } yield novaReserva
    
    resultado.value
  }

  def buscarReservasPorQuartoId(idQuarto: Long): Future[Seq[ReadReservaDto]] = {
    reservaRepository.possuiReservaPorQuartoId(idQuarto).map {
      reservasQuarto => ReadReservaDto.fromReservas(reservasQuarto)
    }
  }

  def buscarReservaPorHospedeId(idHospede: Long): Future[Seq[ReadReservaDto]] = {
    reservaRepository.buscarReservaPorHospedeID(idHospede).map {
      reservasHospede => ReadReservaDto.fromReservas(reservasHospede)
    }
  }

  def buscarReservaPorId(id: Long): Future[Option[ReadReservaDto]] = {
    reservaRepository.buscarReservaPorId(id).map {
      case Some(reserva) => Some(ReadReservaDto.fromReserva(reserva))
      case None => None
    }
  }
  
  def buscarReservas(): Future[Seq[ReadReservaDto]] = {
    reservaRepository.buscarReservas().map(ReadReservaDto.fromReservas)
  }

  def atualizarReserva(dto: UpdateReservaDto): Future[Int] = {
    reservaRepository.atualizarReserva(Reserva.fromUpdateReservaDto(dto))
  }

  def deletarResservaPorId(id: Long): Future[Either[ReservaError, Int]] = {
    buscarReservaPorId(id).flatMap {
      case Some(_) =>
        reservaRepository.deletarReserva(id).map {
          case 0 => Left(ErroInesperado(s"Erro ao deletar a reserva com ID: $id"))
          case itensAlterados => Right(itensAlterados)
        }
      case None => Future.successful(Left(ReservaNaoEncontrada))
    }.recover {
      case ex: Exception => Left(ErroInesperado(ex.getMessage))
    }
  }

  private def buscarExisteReservaQuartoPorIdData(idQuarto: Long, dataEntrada: LocalDateTime, dataSaida: LocalDateTime): Future[Boolean] = {
    reservaRepository.buscarPossuiReservaDentroPeriodoPorIdQuarto(idQuarto, dataEntrada, dataSaida)
  }

  private def buscarExistReservaHospedePorId(idHospede: Long): Future[Boolean] = {
    reservaRepository.buscarReservaPorHospedeID(idHospede).map {
      reservasHospede => reservasHospede.isEmpty
    }
  }

  private def validarQuartoCadastrado(id: Long): Future[Either[ReservaError, Unit]] = {
    quartoService.buscarQuartoPorId(id).map {
      case Some(_) => Right(())
      case None => Left(QuartoNaoEncontrado)
    }
  }

  private def validarDisponibilidadeQuarto(reservaDto: CreateReservaDto, tempoLimpeza: Int): Future[Either[ReservaError, Unit]] ={
    val horarioNovaReservaEValido =
      buscarExisteReservaQuartoPorIdData(
        reservaDto.quartoId,
        reservaDto.dataEntrada.minusHours(tempoLimpeza),
        reservaDto.dataSaida.plusHours(tempoLimpeza))
    horarioNovaReservaEValido.map {
      case true => Left(QuartoIndisponivel)
      case _ => Right(())
    }
  }

  private def validarHospedeCadastrado(id: Long): Future[Either[ReservaError, Unit]] = {
    hospedeService.buscarHospedePorId(id).map {
      case Some(_) => Right(())
      case None => Left(HospedeNaoEncontrado)
    }
  }

  private def validarHospedePossuiReserva(id: Long): Future[Either[ReservaError, Unit]] = {
    reservaRepository.buscarReservaPorHospedeID(id).map { totalReservas =>
      totalReservas.isEmpty match
      case true => Right(())
      case _ => Left(HospedeIndisponivel)
    }
  }
  
  private def deletarReservaBando(id: Long): Future[Either[ReservaError, Int]] = {
    reservaRepository.deletarReserva(id).map(itensAlterados => Right(itensAlterados))
  }

  private def gravarReservaBanco(dto: CreateReservaDto): Future[Either[ReservaError, ReadReservaDto]] = {
    reservaRepository.criarReserva(Reserva.fromCreateReservaDto(dto))
      .map(reserva => Right(ReadReservaDto.fromReserva(reserva)))
  }
}

sealed trait ReservaError

case object QuartoNaoEncontrado extends ReservaError
case object QuartoIndisponivel extends ReservaError
case object HospedeNaoEncontrado extends ReservaError
case object HospedeIndisponivel extends ReservaError
case object ReservaNaoEncontrada extends ReservaError
case class ErroInesperado(mensagem: String) extends ReservaError
