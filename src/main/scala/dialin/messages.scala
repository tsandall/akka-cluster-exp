package dialin

case object BackendRegistration
case class TransformationJob(text: String)
case class TransformationResult(text: String)
case class TransformationFailure(message: String, job: TransformationJob)