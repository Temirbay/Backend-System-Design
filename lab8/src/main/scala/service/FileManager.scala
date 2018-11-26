package service

import java.io.File

import akka.actor.{Actor, ActorLogging, Props}
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.{CreateBucketRequest, GetBucketLocationRequest}

object FileManager {
  case object StartFilesCopy
  case object StartFilesRead

  def props(s3Client : AmazonS3, bucketName : String) =
    Props (new FileManager(s3Client, bucketName))
}

class FileManager(s3Client: AmazonS3, bucketName: String)
  extends Actor with ActorLogging {
  import FileManager._

  // create a bucket if it does not exist

  if (!s3Client.doesBucketExistV2(bucketName)) {
    // Because the CreateBucketRequest object doesn't specify a region, the
    // bucket is created in the region specified in the client.
    s3Client.createBucket(new CreateBucketRequest(bucketName))

    // Verify that the bucket was created by retrieving it and checking its location.
    val bucketLocation = s3Client.getBucketLocation(new GetBucketLocationRequest(bucketName))
    log.info(s"Bucket location: $bucketLocation")
  } else {
    log.info("Such bucket already exists.")
  }

  val fileUploader = context.actorOf(FileUploader.props(s3Client, bucketName))

  val fileReader = context.actorOf(FileReader.props (s3Client, bucketName))

  override def receive: Receive =  {

    case StartFilesCopy =>
      val currentDir = new File ("./lab-8-in")
      invokeDirectory(currentDir.getPath)

    case StartFilesRead =>
      fileReader ! FileReader.ReadFiles ()

  }

  def invokeDirectory (path : String): Unit = {
    val files = new File (path).listFiles().toList
    files.foreach(file => {
        if (file.isDirectory) {
          invokeDirectory(file.getPath)
        } else {
          fileUploader ! FileUploader.UploadFile(file.getPath)
        }
    })
  }
}
