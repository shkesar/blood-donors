package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import models.Donor
import models.FindData

object Find extends Controller {

	private val findForm: Form[FindData] = Form(
		mapping(
			"locality" -> nonEmptyText,
			"bloodGroup" -> nonEmptyText
		)(FindData.apply)(FindData.unapply)
	)

	def find = Action {
		Ok(views.html.find(findForm))
	}

	def result = Action { implicit request =>
		val findData = findForm.bindFromRequest.get
		val donors = models.Donors.find(findData.locality, findData.bloodGroup)

		donors match {
			case list: List[Donor] if !list.isEmpty => {
				Ok(views.html.list(donors))
			}
			case _ =>
				NotFound
		}
	}

}