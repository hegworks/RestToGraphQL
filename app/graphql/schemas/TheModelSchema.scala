package graphql.schemas

import javax.inject._

import sangria.schema._

import controllers.TheController
import models.TheModel

class TheModelSchema @Inject() (theController: TheController){

	/**
	  * "Sangria GraphQL object" Defenition of TheModel object
	  */
	implicit val TheModelType = ObjectType(
		name = "TheModel",
		description = "this is a theModel",
		fields[Unit,TheModel](
			Field("_id", StringType, resolve = _.value._id.toString()),
			Field("name", StringType, resolve = _.value.name),
			Field("description", StringType, resolve = _.value.description),
			Field("number", IntType, resolve = _.value.number)
	   )
   )

   	/**
	 * the top-level fields 
	 * will use these fields as an entry point in GraphQL queries
	 */
	val Queries: List[Field[Unit,Unit]] = List(
	Field(
		name = "findTheModel",
		fieldType = OptionType(TheModelType),
		description = Some("Returns themodel with matching id"),
		arguments = List(
			Argument("_id", StringType)
		),
		resolve =
			sangriaContext =>
			theController.gqlFindOne(sangriaContext.args.arg[String]("_id"))
		)
	)
}