package service

import java.io.File

import akka.actor.{Actor, ActorLogging, Props}
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.{GetObjectRequest, ListObjectsV2Request, ListObjectsV2Result, S3ObjectSummary}


object FileReader {

  case class ReadFiles ()

  def props(s3Client: AmazonS3, bucketName : String)
      = Props (new FileReader(s3Client, bucketName))

}

class FileReader(s3Client: AmazonS3, bucketName : String)
  extends Actor with ActorLogging {
  import FileReader._

  override def receive: Receive = {
    case ReadFiles() =>
      val req = new ListObjectsV2Request().withBucketName(bucketName)

      var result: ListObjectsV2Result = null
      do {
        result = s3Client.listObjectsV2(req)

        val list = result.getObjectSummaries
        list.forEach(objectSummary => {
          val tempFile = s"./lab-8-out${objectSummary.getKey.substring(8, objectSummary.getKey.length)}"

          val file = new File (tempFile)
          log.info(s"Downloading $tempFile ...")

          val request = new GetObjectRequest(bucketName, objectSummary.getKey)
          s3Client.getObject(request, file)
        })
        log.info(s"Downloading finished.")

      } while (result.isTruncated)
  }
}