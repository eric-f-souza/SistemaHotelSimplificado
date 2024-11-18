package controllers

import Util.FormatadorMensagens
import dto.{CreateReservaDto, UpdateReservaDto}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import services.{ErroInesperado, HospedeIndisponivel, HospedeNaoEncontrado, QuartoIndisponivel, QuartoNaoEncontrado, ReservaNaoEncontrada, ReservaService}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ReservaController @Inject()(
                                   cc: ControllerComponents,
                                   reservaService: ReservaService
                                 )
                                 (implicit ec: ExecutionContext) extends AbstractController(cc) {

  def criarReserva(tempoLimpeza: Int) = Action.async(parse.json) { jsValue =>
    jsValue.body.validate[CreateReservaDto].fold(
      erros => {
        Future.successful(
          BadRequest(
            Json.obj(
              FormatadorMensagens.mensagenErroConversaoJson(),
              FormatadorMensagens.formatarMensagemErroConversaoJson(erros))))
      },

      dto => {
        reservaService.criarReserva(dto, tempoLimpeza).map {
          case Right(readReservaDto) => Created(Json.toJson(readReservaDto))
          case Left(QuartoIndisponivel)
            => BadRequest(Json.obj("erro" -> s"Quarto de id ${dto.quartoId} não está disponível no horário solicitado"))
          case Left(QuartoNaoEncontrado)
            => BadRequest(Json.obj(FormatadorMensagens.formatarMensagemItemNaoEncontrado("Quarto", dto.quartoId)))
          case Left(HospedeIndisponivel)
            => BadRequest(Json.obj("erro" -> s"Hospede de id ${dto.hospedeId} já possui uma reserva"))
          case Left(HospedeNaoEncontrado)
            => BadRequest(Json.obj(FormatadorMensagens.formatarMensagemItemNaoEncontrado("Hospede", dto.hospedeId)))
        }
      }
    )
  }
  
  def buscarReservaPorId(id: Long) = Action.async {
    reservaService.buscarReservaPorId(id).map {
      case Some(readReservaDto) => Ok(Json.toJson(readReservaDto))
      case None => NotFound(Json.obj(FormatadorMensagens.formatarMensagemItemNaoEncontrado("Reserva", id)))
    }.recover {
      case ex: Exception => InternalServerError(Json.obj(FormatadorMensagens.formatarMennsagemExcecao(ex.getMessage)))
    }
  }
  
  def buscarTodasAsReservas() = Action.async {
    reservaService.buscarReservas().map { readReservasDto =>
      Ok(Json.toJson(readReservasDto))
    }
  }
  
  def buscarReservasPorQuartoId(idQuarto: Long) = Action.async {
    reservaService.buscarReservasPorQuartoId(idQuarto).map { reservasQuarto =>
      Ok(Json.toJson(reservasQuarto))
    }
  }
  
  def buscarReservasPorHospedeId(idHospede: Long) = Action.async {
    reservaService.buscarReservaPorHospedeId(idHospede).map { reservasHospede =>
      Ok(Json.toJson(reservasHospede))
    }
  }
  
  def atualizarReserva(id: Long) = Action.async(parse.json) { json => 
    json.body.validate[UpdateReservaDto].fold(
      erros => {
        Future.successful(
          BadRequest(
            Json.obj(
              FormatadorMensagens.mensagenErroConversaoJson(),
              FormatadorMensagens.formatarMensagemErroConversaoJson(erros))))
      },

      dto => {
        reservaService.atualizarReserva(dto).map { totalItensAlterados => 
          if (totalItensAlterados > 0) Ok(Json.obj(FormatadorMensagens.formatarMensagemAtualizacao("Reserva")))
          else NotFound(Json.obj(FormatadorMensagens.formatarMensagemItemNaoEncontrado("Reserva", id)))
        }
      }
    )
  }
  
  def deletarReservaPorId(id: Long): Action[AnyContent] = Action.async {
    reservaService.deletarResservaPorId(id).map {
      case Right(itensAlterados) => 
        if(itensAlterados > 0)Ok(Json.obj(FormatadorMensagens.formatarMensagemDeletado("Reserva")))
        else InternalServerError(Json.obj(FormatadorMensagens.formatarMensagemErroAoDeletar("reserva", id)))
      case Left(ReservaNaoEncontrada)
      => NotFound(Json.obj(FormatadorMensagens.formatarMensagemItemNaoEncontrado("Reserva", id)))
    }.recover {
      case ex: Exception => InternalServerError(Json.obj(FormatadorMensagens.formatarMennsagemExcecao(ex.getMessage)))
    }
  }
}
