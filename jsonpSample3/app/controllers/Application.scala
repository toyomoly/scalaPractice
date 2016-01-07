package controllers

import play.api._
import play.api.mvc._
import play.api.libs._
import play.api.libs.json._
import play.api.db._
import play.api.Play.current
import scala.collection.mutable.ListBuffer
import play.api.libs.ws._
import scala.concurrent.Future

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  class ball(var x: Int = 0, var y: Int = 0, var r: Int = 10, var vx: Int = 1, var vy: Int = 1) {
  }

  implicit object BallWrites extends Writes[ball] {
    def writes(e: ball): JsValue = {
      Json.obj(
        "x" -> e.x,
        "y" -> e.y,
        "r" -> e.r,
        "vx" -> e.vx,
        "vy" -> e.vy)
    }
  }

  //  def index = Action {
  //    Ok(views.html.index("Your new application is ready."))
  //  }

  def getBall(pattern: Int) = Action {
    Ok(this._getBallJson(pattern))
  }

  def getBallWithJsonp(pattern: Int, callback: String) = Action {
    Ok(Jsonp(callback, this._getBallJson(pattern)))
  }

  def _getBallJson(pattern: Int) = {
    var balls = ListBuffer[ball]()

    // access "default" database
    DB.withConnection { conn =>
      var stmt = conn.createStatement()
      var rs = stmt.executeQuery("SELECT * FROM BALLDATA WHERE PATTERN = " + pattern)

      while (rs.next()) {
        balls += new ball(rs.getInt("X"), rs.getInt("Y"), rs.getInt("R"), rs.getInt("VX"), rs.getInt("VY"))
      }

      rs.close()
      stmt.close()
    }

    Json.toJson(balls)
  }

}
