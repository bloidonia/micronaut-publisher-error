package micronaut.publisher.error

import io.micronaut.core.annotation.NonNull
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import org.reactivestreams.Publisher
import spock.lang.Specification

import javax.validation.constraints.NotBlank
//import jakarta.validation.constraints.NotBlank

@MicronautTest
class MicronautPublisherErrorSpec extends Specification {

    @Inject
    TestClient client

    def "publisher based validated response is encoded as Json"() {
        when:
        client.publisher("Tim")

        then:
        HttpClientResponseException e = thrown(HttpClientResponseException)
        e.status == HttpStatus.BAD_REQUEST
        e.getResponse().getBody(Map).isPresent()
        e.getResponse().getBody(Map).get()._embedded.errors[0].message == 'Hello Tim'
    }

    def "raw string based validated response is encoded as Json"() {
        when:
        client.raw("Tim")

        then:
        HttpClientResponseException e = thrown(HttpClientResponseException)
        e.status == HttpStatus.BAD_REQUEST
        e.getResponse().getBody(Map).isPresent()
        e.getResponse().getBody(Map).get()._embedded.errors[0].message == 'Hello Tim'
    }

    @Client("/test")
    static interface TestClient {

        @Get(uri = "/publisher/{feature}", consumes = MediaType.TEXT_PLAIN)
        String publisher(@NonNull @NotBlank String feature);

        @Get(uri = "/raw/{feature}", consumes = MediaType.TEXT_PLAIN)
        String raw(@NonNull @NotBlank String feature);
    }

    @Controller("/test")
    static class TestController {

        @Get(uri = "/publisher/{feature}", produces = MediaType.TEXT_PLAIN)
        Publisher<String> publisher(@NonNull @NotBlank String feature) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Hello $feature")
        }

        @Get(uri = "/raw/{feature}", produces = MediaType.TEXT_PLAIN)
        String raw(@NonNull @NotBlank String feature) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Hello $feature")
        }
    }

}
