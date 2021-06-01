package models

import play.api.libs.json.{Format, Json}
import play.api.libs.json.JodaWrites._
import play.api.libs.json.JodaReads._

import reactivemongo.play.json._
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson._

import sangria.schema._
import sangria.macros.derive._

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
  /**
   * deriveObjectType[Ctx, Val]
   * constructs an ObjectType[Ctx, Val] with fields found in Val class (case class accessors and members annotated with @GraphQLField)
   * 
   * Ctx: represents some contextual object that flows across the whole execution (and doesn’t change in most of the cases). It can be provided to execution by the user in order to help fulfill the GraphQL query. A typical example of such a context object is a service or repository object that is able to access a database.
   * 
   * Val: represent values that are returned by the resolve function and given to the resolve function as a part of the Context.
   * */


   val TheModelType = ObjectType(
	   "name",
	   "desc",
	   fields[Unit,TheModel](
		   Field("_id", StringType, resolve = _.value._id.toString()),
		   Field("name", StringType, resolve = _.value.name),
		   Field("description", StringType, resolve = _.value.description),
		   Field("number", IntType, resolve = _.value.number)
	   )
   )
// 	 implicit val bsonid : TheModel._id = _id.toString()
//   implicit val TheModelType = deriveObjectType[Unit, TheModel](
// 	ObjectTypeName("TheModel"),
// 	ObjectTypeDescription("TheModel's GraphQL object type"),
// 	DocumentField("name", "Name of your TheModel"),
// 	DocumentField("description", "Some description for TheModel"),
// 	DocumentField("number", "A number, Any number!")
// )


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