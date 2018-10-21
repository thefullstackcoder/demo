
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HelloSimulation extends Simulation {

	val httpProtocol = http
		.baseURL("http://localhost:8090")
		.acceptHeader("image/webp,image/apng,image/*,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-US,en;q=0.8")
		.userAgentHeader("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
		"Upgrade-Insecure-Requests" -> "1")



	val scn = scenario("GreetingSimulation")
		.exec(http("request_0")
			.get("/hello")
			.headers(headers_0))

	setUp(
	    scn.inject(
	        rampUsers(50) over (10 seconds), 
	        constantUsersPerSec(50) during (60 seconds)
	    )
	).protocols(httpProtocol)
}
