package controllers


/* 
* without mongodb
*/

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import models.{TheModel}

class TheController @Inject() (cc: ControllerComponents)
    extends AbstractController(cc) {

  implicit val TheModelFormat = Json.format[TheModel]

  def getAll = Action {
    val themodel = new TheModel(1, "name1", "descriptions of name 1")
    Ok(Json.toJson(themodel))
  }

  def add(num1: Int, num2: Int) =  Action {
    var result = num1 + num2
	Ok("" + num1 + " + " + num2 + " = " + result + "")
  }
}




/*
import javax.inject.Inject

import scala.concurrent.{ExecutionContext, Future}

import play.api.Logger
import play.api.mvc.{AbstractController, ControllerComponents}
import play.api.libs.json._

import reactivemongo.api.Cursor

import reactivemongo.api.bson.collection.BSONCollection

import play.modules.reactivemongo.{
  MongoController,
  ReactiveMongoApi,
  ReactiveMongoComponents
}

// to make sure JSON/BSON conversions are available
import reactivemongo.play.json.compat._,
json2bson.{toDocumentReader, toDocumentWriter}

import models.{TheModel}

/*
 * This controller uses case classes and their associated Reads/Writes to read or write JSON structures.
 */

// Play's dependency injection mechanism to resolve instance of  ReactiveMongoApi which as an interface to MongoDB:
class TheController @Inject() (
    components: ControllerComponents,
    val reactiveMongoApi: ReactiveMongoApi
) extends AbstractController(components)
    with MongoController
    with ReactiveMongoComponents {

  implicit def ec: ExecutionContext = components.executionContext

  def collection: Future[BSONCollection] =
    database.map(_.collection[BSONCollection]("thecollection"))

  // Using case classes + JSON Writes and Reads
  import models._
  import models.JsonFormats._


  def createFromJson = Action.async(parse.json) { request =>
    /*
 * request.body is a JsValue.
 * There is an implicit Writes that turns this JsValue as a JsObject,
 * so you can call insert.one() with this JsValue.
 * (insert.one() takes a JsObject as parameter, or anything that can be
 * turned into a JsObject using a Writes.)
 */
    request.body.validate[TheModel].map { themodel =>
      // `themodel` is an instance of the case class `models.TheModel`
      collection.flatMap(_.insert.one(themodel)).map { lastError =>
        Logger(s"Successfully inserted with LastError: $lastError")
        Created
      }
    }.getOrElse(Future.successful(BadRequest("invalid json")))
  }



  def getAll = Action {
    val themodel = new TheModel(1, "name1", "descriptions of name 1")
    Ok(themodel.toString())
  }
}
 */
