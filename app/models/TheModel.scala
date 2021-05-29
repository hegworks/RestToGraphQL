package models

import play.api.libs.json.{Format, Json}
import reactivemongo.play.json._
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson._
import play.api.libs.json.JodaWrites._
import play.api.libs.json.JodaReads._

/**
  * we have a basic case class that contains the definition of
  * a theModel and a companion object that contains the implicit JSON/BJSON serializers.
  *
  * @param id
  * @param name
  * @param description
  */
case class TheModel(
                  id:Option[BSONObjectID],
                  name:String,
                  description:String
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
        doc.getAs[String]("description").get)
    }
  }

  implicit object TheModelBSONWriter extends BSONDocumentWriter[TheModel] {
    def write(theModel: TheModel): BSONDocument = {
      BSONDocument(
        "ـid" -> theModel.id,
        "name" -> theModel.name,
        "description" -> theModel.description

      )
    }
  }
}