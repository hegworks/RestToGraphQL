package models

import play.api.libs.json.Json

case class TheModel(
	id: Long, 
	name: String, 
	description: String)

	object JsonFormats {
		// Generates Writes and Reads for TheModel using Json Macros
		implicit val TheModelFormat = Json.format[TheModel]
	}