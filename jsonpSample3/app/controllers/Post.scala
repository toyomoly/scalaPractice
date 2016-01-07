package controllers

import play.api._
import play.api.mvc._
//import play.api.libs._
import play.api.libs.json._
//import play.api.db._
//import play.api.Play.current
//import scala.collection.mutable.ListBuffer
import play.api.libs.ws._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext._
// import ExecutionContext.Implicits.global
import play.api.libs.concurrent.Execution.Implicits._

object Post extends Controller {

  def getSectionList = Action.async {
    Logger.debug("[getSectionList] start")
    val result: Future[Response] = WS.url("http://yukisaki/WebService.asmx/GetSectionList").post(Json.obj())
    result.map { response =>
      Logger.debug("[getSectionList] ok")
      Ok(response.body)
    } recover {
      case e: java.net.ConnectException => Ok("[getSectionList] error")
    }
  }

  def getYukisakiList = Action.async {
    Logger.debug("[getYukisakiList] start")
    val result: Future[Response] = WS.url("http://yukisaki/WebService.asmx/GetYukisakiList").post(Json.obj())
    result.map { response =>
      Logger.debug("[getYukisakiList] ok")
      Ok(response.body)
    } recover {
      case e: java.net.ConnectException => Ok("[getYukisakiList] error")
    }
  }

  def updateYukisaki(v1: Int, v2: Int) = Action.async {
    Logger.debug("[updateYukisaki] start")
    val result: Future[Response] = WS.url("http://yukisaki/WebService.asmx/UpdateYukisaki").post(Json.obj(
      "UserID" -> "n2817",
      "StatusCD" -> "1",
      "Jotai" -> "",
      "Yukisaki" -> "",
      "Nichiji" -> ""))
    //    val result: Future[Response] = WS.url("http://172.16.16.33/n2817/KenshuWebService/Service1.asmx/PlusMethod").post(Json.obj(
    //      "val1" -> v1,
    //      "val2" -> v2))
    result.map { response =>
      Logger.debug("[updateYukisaki] ok")
      Ok(response.body)
    } recover {
      case e: java.net.ConnectException => Ok("[updateYukisaki] error")
    }
  }

  def updateYukisakiG = Action.async {
    Logger.debug("[updateYukisaki] start")
    //val result: Future[Response] = WS.url("http://yukisaki/WebService.asmx/UpdateYukisaki").post(Json.obj())
    val result: Future[Response] = WS.url("http://172.16.16.33/n2817/KenshuWebService/Service1.asmx/PlusMethod").post(Json.obj(
      "val1" -> 1,
      "val2" -> 2))
    result.map { response =>
      Logger.debug("[updateYukisaki] ok")
      Ok(response.body)
    } recover {
      case e: java.net.ConnectException => Ok("[updateYukisaki] error")
    }
  }

  def getYukisaki3 = Action.async {
    val result: Future[Response] = WS.url("http://172.16.16.33/n2817/KenshuWebService/Service1.asmx/HelloWorld").post(Json.obj())
    result.map { response =>
      Ok(response.body)
    }
  }

  def getYukisaki2 = Action.async {
    val result: Future[Response] = WS.url("http://172.16.16.33/n2817/KenshuWebService/Handler2.ashx").get()
    result.map { response =>
      //Logger.debug(response.body)
      //Ok(response.body).as("text/html")
      Ok(response.body)
    }

    // .setQueryParameter("param1", "foo")
  }

}
