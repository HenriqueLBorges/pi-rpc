import com.pi.thrift.PiService
import com.twitter.finagle.Thrift
import com.twitter.util.{Await, Future}

object Server {
  var pi : BigDecimal = 0.0
  var index = -1
  val port = Integer.valueOf(scala.util.Properties.envOrElse("PORT", "8080"))
  val host = scala.util.Properties.envOrElse("HOST", "0.0.0.0")
  val server = Thrift.server.serveIface(
    host + ":" + port,
    new PiService[Future] {
      def getNumber(): Future[Long] = {
        println("getNumber request")
        index += 1
        Future.value(index)
      }

      def submitNumber(number: String): Future[Unit] = {
        println("submitNumber request")
        pi = pi.+(BigDecimal(number))
        println("New PI value " + pi)
        Future.value(Unit)
      }

      def getPI(): Future[String] = {
        println("GetPI request")
        Future.value(pi.toString())
      }

    }
  )

  def main(args: Array[String]): Unit = {
    println("Starting pi-rpc-server on " + host + ":" + port)
    Await.result(server)
  }
}
