import akka.actor.ActorSystem
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}
import service.{FileManager}

object Application extends App {
  val bucketName = "lab8-files"

  import com.amazonaws.auth.BasicAWSCredentials

  val awsCreds = new BasicAWSCredentials("AKIAIEWZLUTK7IWVRCZQ",
    "MEaeB7PXfn1DjBCcjoggnnWNi/gy64GJpW3Tzki6")

  val s3Client: AmazonS3 = AmazonS3ClientBuilder.standard()
    .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
    .withRegion(Regions.EU_CENTRAL_1)
    .build()

  implicit val system = ActorSystem()

  val fileManager = system.actorOf(FileManager.props(s3Client, bucketName), "file-manager")

  // start copying files from one folder to another
  //fileManager ! FileManager.StartFilesCopy
  fileManager ! FileManager.StartFilesRead

}
