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
			Field("_id", StringType, resolve = _.value._id.toString().slice(19,43)),
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
		),
	Field(
		name = "findAll",
		fieldType = ListType(TheModelType),
		description = Some("Returns all themodels"),
		resolve = _ => theController.gqlFindAll
		)
	)

	val Mutations: List[Field[Unit, Unit]] = List(
		Field(
			name = "addTheModel",
			fieldType = TheModelType,
			arguments = List(
				Argument("name", StringType),
				Argument("description", StringType),
				Argument("number", IntType)
		),
		resolve = sangriaContext =>
			theController.gqlCreate(
				sangriaContext.args.arg[String]("name"),
				sangriaContext.args.arg[String]("description"),
				sangriaContext.args.arg[Int]("number")
			)
		),
		Field(
			name = "updateTheModel",
			fieldType = TheModelType,
			arguments = List(
				Argument("_id", StringType),
				Argument("name", StringType),
				Argument("description", StringType),
				Argument("number", IntType)
		),
		resolve = sangriaContext =>
			theController.gqlUpdate(
				sangriaContext.args.arg[String]("_id"),
				sangriaContext.args.arg[String]("name"),
				sangriaContext.args.arg[String]("description"),
				sangriaContext.args.arg[Int]("number")
			)
		),
		Field(
			name = "deleteTheModel",
			fieldType = BooleanType,
			arguments = List(
				Argument("_id", StringType)
		),
		resolve = sangriaContext =>
			theController.gqlDelete(
				sangriaContext.args.arg[String]("_id")
			)
		)

	)
}