package controllers

import Util.FormatadorMensagens
import dto.{CreateQuartoDto, ReadQuartoDto, UpdateQuartoDto}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import services.QuartoService

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class QuartoController @Inject()(
                                  cc: ControllerComponents,
                                  quartoService: QuartoService
                                )
                                (implicit ec: ExecutionContext) extends AbstractController(cc) {

  def criarQuarto() = Action.async(parse.json) { jsValue =>
    jsValue.body.validate[CreateQuartoDto].fold(
      erros => {
        Future.successful(
          BadRequest(
            Json.obj(
              FormatadorMensagens.mensagenErroConversaoJson(),
              FormatadorMensagens.formatarMensagemErroConversaoJson(erros))))
      },

      dto => {
        quartoService.cadastrar(dto).map { readQuartoDto =>
          Created(Json.toJson(readQuartoDto))
        }.recover {
          case ex: Exception => InternalServerError(Json.obj(FormatadorMensagens.formatarMennsagemExcecao(ex.getMessage)))
        }
      }
    )
  }

  def buscarTodosOsQuartos() = Action.async {
    quartoService.buscarQuartos().map { readQuartoDtos =>
      Ok(Json.toJson(readQuartoDtos))
    }
  }

  def buscarQuartoPorId(id: Long) =  Action.async {
    quartoService.buscarQuartoPorId(id).map {
      case Some(readQuartoDto) => Ok(Json.toJson(readQuartoDto))
      case None => NotFound(Json.obj(FormatadorMensagens.formatarMensagemItemNaoEncontrado("Quarto", id)))
    }.recover {
      case ex: Exception => InternalServerError(Json.obj(FormatadorMensagens.formatarMennsagemExcecao(ex.getMessage)))
    }
  }

  def atualizarQuarto(id: Long) = Action.async(parse.json) { json =>
    json.body.validate[UpdateQuartoDto].fold(
      erros => {
        Future.successful(
          BadRequest(
            Json.obj(
              FormatadorMensagens.mensagenErroConversaoJson(),
              FormatadorMensagens.formatarMensagemErroConversaoJson(erros))))
      },

      dto => {
        quartoService.atualizarQuarto(dto).map { totalItensAlterados =>
          if (totalItensAlterados > 0) Ok(Json.obj(FormatadorMensagens.formatarMensagemAtualizacao("Quarto")))
          else NotFound(Json.obj(FormatadorMensagens.formatarMensagemItemNaoEncontrado("Quarto", id)))
        }
      }
    )
  }

  def deletarQuartoPorId(id: Long): Action[AnyContent] = Action.async {
    quartoService.buscarQuartoPorId(id).flatMap {
      case Some(_) =>
        quartoService.deletarquarto(id).map { itensAlterados =>
          if (itensAlterados > 0) Ok(Json.obj(FormatadorMensagens.formatarMensagemDeletado("Quarto")))
          else InternalServerError(Json.obj(FormatadorMensagens.formatarMensagemErroAoDeletar("quarto", id)))
        }
      case None => Future.successful(NotFound(Json.obj(FormatadorMensagens.formatarMensagemItemNaoEncontrado("Quarto", id))))
    }.recover {
      case ex: Exception => InternalServerError(Json.obj(FormatadorMensagens.formatarMennsagemExcecao(ex.getMessage)))
    }
  }
}
