package Util

import play.api.libs.json.Json.JsValueWrapper
import play.api.libs.json.{JsPath, JsonValidationError}

import scala.collection.Seq

object FormatadorMensagens {
  def formatarMensagemErroConversaoJson(erros: Seq[(JsPath, Seq[JsonValidationError])]): (String, JsValueWrapper) = {
    "Detalhes:" -> erros.map { case (path, erroDeValidacao) =>
      val mensagens = erroDeValidacao.map(_.message).mkString(", ")
      s"Campo: '${path}' Erro(s): $mensagens"
    }.mkString("; ")
  }

  def formatarMensagemAtualizacao(tipo: String): (String, JsValueWrapper) = {
    "status" -> s"$tipo atualizado com sucesso"
  }

  def formatarMensagemErroAtualizacao(tipo: String, id: Long): (String, JsValueWrapper) = {
    "erro" -> s"$tipo com id: $id não encontrado"
  }

  def formatarMensagemDeletado(tipo: String): (String, JsValueWrapper) = {
    "status" -> s"$tipo deletado com sucesso"
  }

  def formatarMensagemErroAoDeletar(tipo: String, id: Long): (String, JsValueWrapper) = {
    "erro" -> s"Erro ao deletar o $tipo de id: $id"
  }

  def formatarMensagemItemNaoEncontrado(tipo: String, id: Long): (String, JsValueWrapper) = {
    "erro" -> s"$tipo com ID $id não encontrado"
  }

  def formatarMennsagemExcecao(msgExcecao: String): (String, JsValueWrapper) = {
    "erro" -> s"Erro inesperado: $msgExcecao"
  }
  
  def mensagenErroConversaoJson(): (String, JsValueWrapper) = {
    "erro" -> "Erro de conversão de Json"
  }
}
