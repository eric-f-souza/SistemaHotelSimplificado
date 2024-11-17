package controllers

import javax.inject.*
import play.api.*
import play.api.mvc.*
import services.DatabaseInicializadorService

import scala.concurrent.ExecutionContext

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(
                                controllerComponents: ControllerComponents, 
                                val dbInicializadorService: DatabaseInicializadorService)
                              (implicit ec: ExecutionContext) extends AbstractController(controllerComponents) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    dbInicializadorService.createTables().map { _ =>
      Ok("Tabelas criadas ou já existentes")
    }
    Ok(views.html.index())
  }
}
