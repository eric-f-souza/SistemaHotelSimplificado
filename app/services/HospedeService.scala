package services

import dto.{CreateHospedeDto, ReadHospedeDto, UpdateHospedeDto}
import models.Hospede
import repositories.HospedeRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class HospedeService @Inject()(hospedeRepository: HospedeRepository)(implicit ec: ExecutionContext) {
  def createTable(): Future[Unit] = {
    hospedeRepository.createTable()
  }

  def cadastrarHospede(createHospedeDto: CreateHospedeDto): Future[ReadHospedeDto] = {
    hospedeRepository.cadastrarHospede(Hospede.fromCreateHospedeDto(createHospedeDto)).map(ReadHospedeDto.fromHospede)
  }

  def buscarHospedePorId(id: Long): Future[Option[ReadHospedeDto]] = {
    hospedeRepository.buscarHospedePorId(id).map {
      case Some(hospede) => Some(ReadHospedeDto.fromHospede(hospede))
      case None => None
    }
  }

  def bucarHospedes(): Future[Seq[ReadHospedeDto]] = {
      hospedeRepository.buscarHospedes().map(ReadHospedeDto.fromHospedes)
  }
  
  def atualizarHospede(updateHospedeDto: UpdateHospedeDto): Future[Int] = {
    hospedeRepository.atualizarHospede(Hospede.fromUpdateHospedeDto(updateHospedeDto))
  }
  
  def deletarHospedePorId(id: Long): Future[Int] = {
    hospedeRepository.deletarHospedePorId(id)
  }
}
