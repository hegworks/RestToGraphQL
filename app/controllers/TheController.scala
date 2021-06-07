package controllers

import javax.inject._

import play.api.mvc._
import play.api.libs.json.{JsValue, Json, __}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

import reactivemongo.bson.BSONObjectID

import models.TheModel
import repositories.TheRepository

/** create the endpoints to expose the actions for the theModels repository.
  *
  * @param executionContext
  * @param theRepository
  * @param controllerComponents
  */
@Singleton
class TheController @Inject() (implicit
    executionContext: ExecutionContext,
    val theRepository: TheRepository,
    val controllerComponents: ControllerComponents
) extends BaseController {
  // actions:

  /** the two endpoints responsible of reading data:
    *
    * this will return the theModel list
    *
    * @return
    */
  def findAll(): Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] =>
      theRepository.findAll().map { theModels =>
        Ok(Json.toJson(theModels))
      }
  }

  /** this will parse the given id and return the associated theModel if it’s found.
    *
    * @param id
    * @return
    */
	def findOne(id: String): Action[AnyContent] = Action.async {
		implicit request: Request[AnyContent] =>
		val objectIdTryResult = BSONObjectID.parse(id)
		objectIdTryResult match {
			case Success(objectId) =>
			theRepository.findOne(objectId).map { theModel =>
				Ok(Json.toJson(theModel))
			}
			case Failure(_) =>
			Future.successful(BadRequest("Cannot parse the theModel id"))
		}
	}


  def gqlFindOne(id: String): Future[Option[TheModel]] = {
	  val objectIdTryResult = BSONObjectID.parse(id)
	  objectIdTryResult match {
        case Success(objectId) =>
          theRepository.findOne(objectId)
		case Failure(_) =>  println("ERROR: Cannot parse the theModel id")
		Future(Some(new TheModel(
			Some(BSONObjectID.generate()),
			"ERROR: Cannot parse the theModel id",
			"ERROR: Cannot parse the theModel id",
			-1)))

      }
  }

    def gqlFindAll: Future[Seq[TheModel]] = {
      theRepository.findAll()
	}

    def gqlCreate(name:String, description:String, number:Int): TheModel = {
		val theModel = new TheModel(
			Some(BSONObjectID.generate()),
			name,
			description,
			number)
		theRepository.create(theModel)
		theModel
    }

	def gqlUpdate(id: String, name:String, description:String, number:Int): Future[TheModel] = {
		val objectIdTryResult = BSONObjectID.parse(id)
		objectIdTryResult match {
			case Success(objectId) =>
				val updatedTheModel = new TheModel(Some(objectId), name, description, number)
				theRepository.update(objectId,updatedTheModel)
				Future(updatedTheModel)
			case Failure(_) =>
				println("ERROR: Cannot parse the theModel id")
				Future(new TheModel(
					Some(BSONObjectID.generate()),
					"ERROR: Cannot parse the theModel id",
					"ERROR: Cannot parse the theModel id",
					-1)
				)
		}
    }

	def gqlDelete(id: String): Boolean = {
        val objectIdTryResult = BSONObjectID.parse(id)
		objectIdTryResult match {
			case Success(objectId) =>
				theRepository.delete(objectId)
				true
			case Failure(_) =>
				println("ERROR: Cannot parse the theModel id")
				false
		}
  }

  def gqlAdd(num1: Int, num2: Int): Int = {
    num1 + num2
  }

  /** validating the id passed in an argument
    * and check if the json is valid by using the validate helper in the request body.
    *
    * Thanks to the json serialization macro,
    * the Scala object can be serialized implicitly from json and vise-versa.
    *
    * @return
    */
  def create(): Action[JsValue] =
    Action.async(controllerComponents.parsers.json) { implicit request =>
      {

        request.body
          .validate[TheModel]
          .fold(
            _ => Future.successful(BadRequest("Cannot parse request body")),
            theModel =>
              theRepository.create(theModel).map { _ =>
                Created(Json.toJson(theModel))
              }
          )
      }
    }

  def update(id: String): Action[JsValue] =
    Action.async(controllerComponents.parsers.json) { implicit request =>
      {
        request.body
          .validate[TheModel]
          .fold(
            _ => Future.successful(BadRequest("Cannot parse request body")),
            theModel => {
              val objectIdTryResult = BSONObjectID.parse(id)
              objectIdTryResult match {
                case Success(objectId) =>
                  theRepository.update(objectId, theModel).map { result =>
                    Ok(Json.toJson(result.ok))
                  }
                case Failure(_) =>
                  Future.successful(BadRequest("Cannot parse the theModel id"))
              }
            }
          )
      }
    }

  def delete(id: String): Action[AnyContent] = Action.async {
    implicit request =>
      {
        val objectIdTryResult = BSONObjectID.parse(id)
        objectIdTryResult match {
          case Success(objectId) =>
            theRepository.delete(objectId).map { _ =>
              NoContent
            }
          case Failure(_) =>
            Future.successful(BadRequest("Cannot parse the theModel id"))
        }
      }
  }

  def add(num1: Int, num2: Int) =  Action {
    var result = num1 + num2
	Ok("" + num1 + " + " + num2 + " = " + result + "")
  }
}