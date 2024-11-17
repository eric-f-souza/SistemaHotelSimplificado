package services

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
final class DatabaseInicializadorService @Inject()(
                                                    db: DatabaseService,
                                                    quartoRepository: QuartoService,
                                                    hospedeRepository: HospedeService,
                                                    reservaRepository: ReservaService
 )(implicit ec: ExecutionContext) {
  def createTables(): Future[Unit] = {
    for {
      _ <- quartoRepository.createTable()
      _ <- hospedeRepository.createTable()
      _ <- reservaRepository.createTable()
    } yield ()
  }
}
