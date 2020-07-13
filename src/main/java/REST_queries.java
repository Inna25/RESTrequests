import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

public class REST_queries {

    public static final String fooResourceUrl = "http://jsonplaceholder.typicode.com/posts";
    public static final int LASTITEMID = 100;
    public static final int STATUSSUCCESS = 201;
    public static final String NOTFOUND = "404 Not Found";

    public static void main(String[] args) throws IOException {
        String messageStr = "";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseString;
        ResponseEntity<Foo> responseFoo;
        Foo foo = new Foo(11, 0, "test title", "test body");

        //POST - Check for object existence
        HttpEntity<Foo> request = new HttpEntity<>(foo);
        foo = restTemplate.postForObject(fooResourceUrl, request, Foo.class);
        assertNotEquals( null, foo);

        //POST - Check success of status
        responseFoo = restTemplate.exchange(fooResourceUrl, HttpMethod.POST, request,
                Foo.class);
        assertEquals(responseFoo.getStatusCode(), HttpStatus.CREATED);

        //POST of empty object
        request = new HttpEntity<>(new Foo());
        responseFoo = restTemplate.exchange(fooResourceUrl, HttpMethod.POST, request, Foo.class);
        assertEquals(responseFoo.getStatusCode(), HttpStatus.CREATED);
        assertEquals(STATUSSUCCESS, responseFoo.getStatusCodeValue());
        assertEquals(0, responseFoo.getBody().getUserId());

        //Get
        responseString = restTemplate.getForEntity(fooResourceUrl + "/1", String.class);
        assertEquals(responseString.getStatusCode(), HttpStatus.OK);

        //GET to class Foo
        foo = restTemplate.getForObject(fooResourceUrl + "/1", Foo.class);
        assertEquals (1, foo.getId());

        //GET non exist item
        try {
            responseString = restTemplate.getForEntity(fooResourceUrl + "/"+ (LASTITEMID + 1), String.class);

        } catch (Exception e) {
            messageStr = e.getMessage();
        }
        assertEquals(NOTFOUND, messageStr);
    }
}
