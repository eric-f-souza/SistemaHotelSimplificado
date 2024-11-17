package controllers

import Util.FormatadorMensagens
import dto.{CreateHospedeDto, UpdateHospedeDto}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import services.HospedeService

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class HospedeController @Inject()(
                                   cc: ControllerComponents, 
                                   hospedeService: HospedeService
                                 )
                                 (implicit ec: ExecutionContext) extends AbstractController(cc) {

  def criarHospede() = Action.async(parse.json) { jsValue =>
    jsValue.body.validate[CreateHospedeDto].fold(
      erros => {
        Future.successful(
          BadRequest(
            Json.obj(
                FormatadorMensagens.mensagenErroConversaoJson(),
                FormatadorMensagens.formatarMensagemErroConversaoJson(erros))))
      },

      dto => {
        hospedeService.cadastrarHospede(dto).map { readHospedeDto =>
          Created(Json.toJson(readHospedeDto))
        }.recover {
          case ex: Exception => InternalServerError(Json.obj("erro" -> s"Erro ao criar hóspede: ${ex.getMessage}"))
        }
      }
    )
  }

  def buscarHospedePorId(id: Long) = Action.async {
    hospedeService.buscarHospedePorId(id).map {
      case Some(readHospedeDto) => Ok(Json.toJson(readHospedeDto))
      case None => NotFound(Json.obj(FormatadorMensagens.formatarMensagemItemNaoEncontrado("Hospede", id)))
    }
  }

  def buscarTodosOsHospedes() = Action.async {
    hospedeService.bucarHospedes().map(readQuartosDto => Ok(Json.toJson(readQuartosDto)))
  }

  def atualizarHospede(id: Long) = Action.async(parse.json) { json =>
    json.body.validate[UpdateHospedeDto].fold(
      erros => {
        Future.successful(
          BadRequest(
            Json.obj(
              FormatadorMensagens.mensagenErroConversaoJson(),
              FormatadorMensagens.formatarMensagemErroConversaoJson(erros))))
      },

      dto => {
        hospedeService.atualizarHospede(dto).map { totalItensAlterados =>
          if (totalItensAlterados > 0) Ok(Json.obj(FormatadorMensagens.formatarMensagemAtualizacao("Hospede")))
          else NotFound(Json.obj(FormatadorMensagens.formatarMensagemErroAtualizacao("Hospede", id)))
        }
      }
    )
  }

  def delearHospedePorId(id: Long): Action[AnyContent] = Action.async {
    hospedeService.buscarHospedePorId(id).flatMap {
      case Some(_) =>
        hospedeService.deletarHospedePorId(id).map { itensAlterados =>
          if (itensAlterados > 0) Ok(Json.obj(FormatadorMensagens.formatarMensagemDeletado("Hospede")))
          else InternalServerError(Json.obj(FormatadorMensagens.formatarMensagemErroAoDeletar("hospede", id)))
        }
      case None => Future.successful(NotFound(Json.obj(FormatadorMensagens.formatarMensagemItemNaoEncontrado("Hospede", id))))
    }.recover {
      case ex: Exception => InternalServerError(Json.obj(FormatadorMensagens.formatarMennsagemExcecao(ex.getMessage)))
    }
  }
}
