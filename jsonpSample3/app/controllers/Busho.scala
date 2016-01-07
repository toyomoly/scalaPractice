package controllers

import play.api._
import play.api.mvc._

import play.api.libs._
import play.api.libs.json._
import play.api.db._
import play.api.Play.current
import scala.collection.mutable.ListBuffer

object Busho extends Controller {

  class busho(var cd: String, var name: String) {
  }

  implicit object BushoWrites extends Writes[busho] {
    def writes(e: busho): JsValue = {
      Json.obj(
        "cd" -> e.cd,
        "name" -> e.name)
    }
  }

  def getBusho() = Action {
    Ok(this._getBushoJson())
  }
  
  def getBushoWithJsonp(callback: String) = Action {
    Ok(Jsonp(callback, this._getBushoJson()))
  }

  def _getBushoJson() = {
    var bushos = ListBuffer[busho]()

    // access "default" database
    DB.withConnection { conn =>
      // ここはもっと他にいい方法があるはず
      var stmt = conn.createStatement()
      var rs = stmt.executeQuery("SELECT * FROM BUSHOMST")

      while (rs.next()) {
        bushos += new busho(rs.getString("BUSHOCD"), rs.getString("BUSHONAME"))
      }

      rs.close()
      stmt.close()
    }

    Json.toJson(bushos)
  }

}