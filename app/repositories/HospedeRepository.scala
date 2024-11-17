package repositories

import models.{Hospede, HospedeTable}
import services.DatabaseService
import slick.jdbc.PostgresProfile.api.*
import slick.lifted.TableQuery

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class HospedeRepository @Inject()(db: DatabaseService)(implicit ex: ExecutionContext) {
  private val hospedes = TableQuery[HospedeTable]

  def createTable(): Future[Unit] = {
    db.run(hospedes.schema.createIfNotExists).map(_ => ())
  }

  def cadastrarHospede(novoHospede: Hospede): Future[Hospede] = {
    db.run(hospedes returning hospedes.map(_.id) into ((hospede, idBanco) => hospede.copy(id = Some(idBanco))) += novoHospede)
  }

  def buscarHospedes(): Future[Seq[Hospede]] = {
    db.run(hospedes.result)
  }

  def buscarHospedePorId(id: Long): Future[Option[Hospede]] = {
    db.run(hospedes.filter(_.id === id).result.headOption)
  }

  def atualizarHospede(hospedeAtualizado: Hospede): Future[Int] = {
    db.run(hospedes.filter(_.id === hospedeAtualizado.id).update(hospedeAtualizado))
  }

  def deletarHospedePorId(id: Long): Future[Int] = {
    db.run(hospedes.filter(_.id === id).delete)
  }
}
