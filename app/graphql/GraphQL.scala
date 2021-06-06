package graphql

import javax.inject._

import sangria.schema.{ObjectType, fields}

import graphql.schemas.TheModelSchema

class GraphQL @Inject() (theModelSchema: TheModelSchema){
	
	/**
	  * Global GraphQL's schema where each model's
	  * queries, mutations and subscrptions are defined.
	  */

	val Schema = sangria.schema.Schema(
		query = ObjectType("Query",
			fields(
				theModelSchema.Queries: _*
			)
		),

		mutation = Some(
			ObjectType("Mutation",
				fields(
					theModelSchema.Mutations: _*
				)
			)
		)
	)
}