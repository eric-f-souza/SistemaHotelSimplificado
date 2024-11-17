package repositories

import models.{Quarto, QuartoTable}
import services.DatabaseService
import slick.jdbc.PostgresProfile.api.*
import slick.lifted.TableQuery

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class QuartoRepository @Inject()(db: DatabaseService)(implicit ex: ExecutionContext) {
  private val quartos = TableQuery[QuartoTable]

  def createTable(): Future[Unit] = {
    db.run(quartos.schema.createIfNotExists).map(_ => ())
  }

  def cadastrarQuarto(quarto: Quarto): Future[Quarto] = {
    db.run((quartos returning quartos.map(_.id) into ((quarto, idBanco) => quarto.copy(id = Some(idBanco)))) += quarto)
  }

  def buscarQuartoPorId(id: Long): Future[Option[Quarto]] = {
    db.run(quartos.filter(_.id === id).result.headOption)
  }

  def buscarQuartos(): Future[Seq[Quarto]] = {
    db.run(quartos.result)
  }

  def atualizarQuarto(quartoAtualizado: Quarto): Future[Int] = {
    db.run(quartos.filter(_.id === quartoAtualizado.id).update(quartoAtualizado))
  }

  def deletarQuartoPorId(id: Long): Future[Int] = {
    db.run(quartos.filter(_.id === id).delete)
  }
}
