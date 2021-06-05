package models

import play.api.libs.json.{Format, Json}
import play.api.libs.json.JodaWrites._
import play.api.libs.json.JodaReads._

import reactivemongo.play.json._
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson._

/**
* a basic case class that contains the definition of
* a theModel and a companion object that contains
* the implicit JSON/BJSON serializers.
*
* @param id
* @param name
* @param description
* @param number
*/
case class TheModel(
  _id:Option[BSONObjectID],
  name:String,
  description:String,
  number:Int
)

/**
* For JSON serialization we’re using automated mapping.
* Basically, the Json.format[TheModel]macro will inspect
* the theModel case class fields and produce a JSON.
* For BSON, however, we’re implementing our custom serializer.
*/
object TheModel{
  implicit val fmt : Format[TheModel] = Json.format[TheModel]

  implicit object TheModelBSONReader extends BSONDocumentReader[TheModel] {
    def read(doc: BSONDocument): TheModel = {
      TheModel(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[String]("name").get,
        doc.getAs[String]("description").get,
        doc.getAs[Int]("number").get)
    }
  }

  implicit object TheModelBSONWriter extends BSONDocumentWriter[TheModel] {
    def write(theModel: TheModel): BSONDocument = {
      BSONDocument(
        "ـid" -> theModel._id,
        "name" -> theModel.name,
        "description" -> theModel.description,
        "number" -> theModel.number
      )
    }
  }
}
