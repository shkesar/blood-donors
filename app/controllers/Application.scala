package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import models.Donor

object Application extends Controller {

	private val donorForm: Form[Donor] = Form(
		mapping(
			"name" -> nonEmptyText,
      "address" -> nonEmptyText,
			"contact" -> nonEmptyText,
			"bloodGroup" -> nonEmptyText,
			"locality" -> nonEmptyText,
			"city" -> nonEmptyText
		)(Donor.apply)(Donor.unapply)
	)

  def index = Action {
    Ok(views.html.index("Blood donors"))
  }

  def dashboard = Action {
    Ok(views.html.dashboard(donorForm))
  }

  def save = Action { implicit request =>
  	donorForm.bindFromRequest.fold(
  		hasErrors = { formWithErrors =>
        Logger.info(formWithErrors.errors.map(_.toString).mkString)
        BadRequest(views.html.dashboard(formWithErrors)) 
      },
      success = { donor =>
        models.Donors.add(donor)
        Logger.info(donor.toString)
  			Redirect(routes.Application.dashboard())
  		}
  	)
  }

}