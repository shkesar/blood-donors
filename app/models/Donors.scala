
package models

import anorm.{SimpleSql, SqlQuery, SQL, Row}
import play.api.Logger
import play.api.db.DB
import play.api.Play.current

case class Donor(
  name: String,
  address: String,
  contact: String,
  bloodGroup: String,
  locality: String,
  city: String)

case class FindData (
	locality: String,
	bloodGroup: String
)

object Donors {

  // fixtures
  val fixtureDonors = Array(
    Donor("Pralave Srivastava", "Vaishnavi Vihar", "+919049405386", "O+", "Katraj", "Pune"),
    Donor("Shubham Kesarwani", "Vaishnavi Vihar", "+918308759127", "O+", "Viman Nagar", "Pune"),
    Donor("Rachit Takkar", "Vaishnavi Vihar", "+917507730348", "O+", "Pune City", "Pune")
  )

  // adding temporary donors for testing purpose in H2 inmem database
  // these guys won't be really happy about donating their own blood
  fixtureDonors.foreach(
    add(_)
  )

  def findAll: List[Donor] = DB.withConnection {
    implicit connection =>
      val sql: SqlQuery = SQL("select * from donors order by name asc")

      val list = sql.apply.map(row =>
        Donor(row[String]("name"), row[String]("address"), row[String]("contact"),
          row[String]("bloodGroup"), row[String]("locality"), row[String]("city"))
      )

      Logger.info(list.mkString("\t"))
      list.toList
  }

  // def maxId: Long = DB.withConnection {
  //   implicit connection =>
  //     val sql: SqlQuery = SQL("select id from donors order by id")

  //     sql.apply.map(row =>
  //       row[Long]("id")
  //     ).toList.max
  // }

  def find(locality: String, bloodGroup: String): List[Donor] = DB.withConnection {
    implicit connection =>
      val sql: SimpleSql[Row] = SQL("select * from donors where locality = {locality} and bloodGroup = {bloodGroup}").on(
        "locality" -> locality,
        "bloodGroup" -> bloodGroup
      )
      sql.apply().map(row =>
        Donor(row[String]("name"), row[String]("address"), row[String]("contact"),
          row[String]("bloodGroup"), row[String]("locality"), row[String]("city"))
      ).toList
  }

  def add(donor: Donor): Boolean = DB.withConnection {
    implicit connection =>
      val addedRows = SQL(
        """
          |insert into donors
          |values ({name}, {address}, {contact}, {bloodGroup}, {locality}, {city})
        """.stripMargin).
        on(
          "name" -> donor.name,
          "address" -> donor.address,
          "contact" -> donor.contact,
          "bloodGroup" -> donor.bloodGroup,
          "locality" -> donor.locality,
          "city" -> donor.city
        ).executeUpdate()

        addedRows == 1
  }

  // def update(donor: Donor): Boolean = DB.withConnection {
  //   implicit connection =>
  //     val updatedRows = SQL(
  //       """
  //         |update donors
  //         |set name = {name},
  //         |contact = {contact},
  //         |bloodGroup = {bloodGroup},
  //         |locality = {locality},
  //         |city = {city}
  //         |where id = {id}
  //       """.stripMargin).
  //       on(
  //         "id" -> donor.id,
  //         "name" -> donor.name,
  //         "contact" -> donor.contact,
  //         "bloodGroup" -> donor.bloodGroup,
  //         "locality" -> donor.locality,
  //         "city" -> donor.city
  //       ).executeUpdate()

  //     updatedRows == 1
  // }

  // def delete(donor: Donor): Boolean = DB.withConnection {
  //   implicit connection =>
  //     val deletedRows = SQL("delete from donors where id = {id}").on(
  //       "id" -> donor.id
  //     ).executeUpdate()

  //     deletedRows == 1
  // }

}
