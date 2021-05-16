/*
package repositories

import javax.inject._
import reactivemongo.api.bson.collection.BSONCollection
import play.modules.reactivemongo.ReactiveMongoApi
import scala.concurrent.{ExecutionContext, Future}

import models.TheModel
import reactivemongo.api.{Cursor, ReadPreference}
import reactivemongo.bson.{BSONDocument, BSONObjectID}

@Singleton
class TheRepository @Inject() (implicit
    ec: ExecutionContext,
    reactiveMongoApi: ReactiveMongoApi
) {
  //a function to avoid potential problems in development with play auto reloading.
  def collection: Future[BSONCollection] =
    reactiveMongoApi.database.map(db => db.collection("thecollection"))


	  def findAll(limit: Int = 100): Future[Seq[TheModel]] = {

    collection.map{
      _.find(BSONDocument(), Option.empty[TheModel])
        .cursor[TheModel](ReadPreference.Primary)
        .collect[Seq](limit, Cursor.FailOnError[Seq[TheModel]]())
	}
  }

  def findOne(id: BSONObjectID): Future[Option[TheModel]] = {
    collection.flatMap(_.find(BSONDocument("_id" -> id), Option.empty[TheModel]).one[TheModel])
  }
}

*/
