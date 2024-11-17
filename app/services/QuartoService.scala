package services

import dto.{CreateQuartoDto, ReadQuartoDto, UpdateQuartoDto}
import models.Quarto
import repositories.QuartoRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class QuartoService @Inject()(quartoRespository: QuartoRepository)(implicit ec: ExecutionContext) {
  def createTable(): Future[Unit] = {
   quartoRespository.createTable()
  }

  def cadastrar(createQuartoDto: CreateQuartoDto): Future[ReadQuartoDto] = {
    println("Chamou o Service")
    val novoQuarto = Quarto(None, createQuartoDto.numero, createQuartoDto.tipo)
    println(s"${novoQuarto.numero} e ${novoQuarto.tipo}")
    quartoRespository.cadastrarQuarto(novoQuarto)
      .map(ReadQuartoDto.fromQuarto)
  }

  def buscarQuartos(): Future[Seq[ReadQuartoDto]] = {
    quartoRespository.buscarQuartos().map(ReadQuartoDto.fromQuartos)
  }

  def buscarQuartoPorId(id: Long): Future[Option[ReadQuartoDto]] = {
    quartoRespository.buscarQuartoPorId(id).map {
      case Some(quarto) => Some(ReadQuartoDto.fromQuarto(quarto))
      case None => None
    }
  }

  def atualizarQuarto(updateQuartoDto: UpdateQuartoDto): Future[Int] = {
    val quartoAtualizado = Quarto.fromUpdateQuartoDto(updateQuartoDto)
    quartoRespository.atualizarQuarto(quartoAtualizado)
  }

  def deletarquarto(id: Long): Future[Int] = {
    quartoRespository.deletarQuartoPorId(id)
  }
}
