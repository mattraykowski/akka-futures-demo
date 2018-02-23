sealed trait SimpleMessages
final case class GetWidget(name: String) extends SimpleMessages
final case object GetFromDB extends SimpleMessages
final case object GetFailure extends SimpleMessages
final case class ObjectFromDB(name: String) extends SimpleMessages