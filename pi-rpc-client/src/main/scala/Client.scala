import com.twitter.finagle.Thrift
import com.twitter.util.Await
import com.pi.thrift.PiService

object Client {

  def main(args: Array[String]): Unit = {
    val service: PiService.FutureIface = Thrift.client.newIface[com.pi.thrift.PiService.FutureIface](scala.util.Properties.envOrElse("HOST", "0.0.0.0")+":"+Integer.valueOf(scala.util.Properties.envOrElse("PORT", "8080")))

    while(true){
      println("new request...")
      var k = 0.0

      Await.result(service.getNumber().onSuccess(value => {
        k = value
      }).onFailure(error => {
        println("error getNumber = " + error)
      }))

      val result : BigDecimal = (1.0/scala.math.pow(16,k))*(4.0/(8.0*k+1.0) - 2.0/(8.0*k+4.0) - 1.0/(8.0*k+5.0) - 1.0/(8.0*k+6.0))

      Await.result(service.submitNumber(result.toString()))
      var pi = ""

      Await.result(service.getPI().onSuccess(value => {
        pi = value
      }).onFailure(error => {
        println("error getPI = " + error)
      }))
      println("pi = " + pi)
    }
  }
}