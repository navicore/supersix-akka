package onextent.supersix.akka.models

import java.time.{ZoneOffset, ZonedDateTime}
import java.util.UUID

final case class Master(alias: String,
                        who: UUID,
                        location: UUID,
                        datetime: ZonedDateTime = ZonedDateTime.now(ZoneOffset.UTC),
                        id: UUID = UUID.randomUUID())

final case class LoadMasterRequest(uri: String, kind: String)

