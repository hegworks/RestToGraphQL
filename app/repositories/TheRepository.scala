package repositories

import javax.inject._
import play.modules.reactivemongo.ReactiveMongoApi
import scala.concurrent.{ExecutionContext, Future}

import reactivemongo.api.{Cursor, ReadPreference}
import reactivemongo.api.commands.WriteResult
import reactivemongo.api.bson.compat._
import reactivemongo.api.bson.collection.BSONCollection
import reactivemongo.bson.{BSONDocument, BSONObjectID}

import models.TheModel

/** injects the execution context and the reactive mongo api.
  * Also, it contains a helpers function that returns a FutureofBSONCollection.
  *
  * @param executionContext
  * @param reactiveMongoApi
  */
@Singleton
class TheRepository @Inject() (implicit
    executionContext: ExecutionContext,
    reactiveMongoApi: ReactiveMongoApi
) {

  /** The `collection` is a function to avoid potential problems in development
    * with play auto reloading.
    *
    * @return
    */
  def collection: Future[BSONCollection] =
    reactiveMongoApi.database.map(db => db.collection("theModels"))

  /** The find method takes two arguments,
    * the selector and the projection.
    * In nutshell, the selector is used to match specific documents
    * and the projector is used to project-specific fields on the documents.
    * In our case, we want to keep things simple and stick with defaults.The find method returns a
    * query builder which means the query is therefore not performed yet.
    * It allows you to add options to the query, like a sorting ordering.
    *
    * @param limit
    * @return
    */
  def findAll(limit: Int = 100): Future[Seq[TheModel]] = {

    collection.flatMap(
      _.find(BSONDocument(), Option.empty[TheModel])
        .cursor[TheModel](ReadPreference.Primary)
        .collect[Seq](limit, Cursor.FailOnError[Seq[TheModel]]())
    )
  }

  def findOne(id: BSONObjectID): Future[Option[TheModel]] = {
    collection.flatMap(
      _.find(BSONDocument("_id" -> id), Option.empty[TheModel]).one[TheModel]
    )
  }

  /** The insert insert method returns an InsertBuilder instance
    * which you can use to insert one or many documents.
    * Same thing for update and delete methods
    * which return an UpdateBuilder and DeleteBuilder respectively.
    *
    * @param theModel
    * @return
    */
  def create(theModel: TheModel): Future[WriteResult] = {
    collection.flatMap(
      _.insert(ordered = false)
        .one(theModel.copy())
    )
  }

  def update(id: BSONObjectID, theModel: TheModel): Future[WriteResult] = {

    collection.flatMap(
      _.update(ordered = false).one(BSONDocument("_id" -> id), theModel.copy())
    )
  }

  def delete(id: BSONObjectID): Future[WriteResult] = {
    collection.flatMap(
      _.delete().one(BSONDocument("_id" -> id), Some(1))
    )
  }
}
