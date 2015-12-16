package tk.sammontakoja.cexml;

import com.eclipsesource.restfuse.Destination;
import com.eclipsesource.restfuse.HttpJUnitRunner;
import com.eclipsesource.restfuse.Method;
import com.eclipsesource.restfuse.Response;
import com.eclipsesource.restfuse.annotation.Context;
import com.eclipsesource.restfuse.annotation.HttpTest;
import org.junit.Rule;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Ari Aaltonen
 *
 * Testing objective steps:
 * 1. Receive HTTP POST request in address:port/camel/food
 * 2. Read XML message from HTTP POST request
 * 3. Transform XML message to Java object
 *
 */
@RunWith( HttpJUnitRunner.class )
public class CamelCanEatCamelFoodTest {

    @Rule
    public Destination destination = new Destination(this, "http://localhost:8080/camel");

    @Context
    private Response response;

    @HttpTest( method = Method.POST, path = "/food/eat", file = "/foo.txt")
    public void posting_text_message_return_http_500() {
        assertThat(response.getStatus(), is(500));
    }

    @HttpTest( method = Method.POST, path = "/food/eat", file = "/DogFood.xml")
    public void posting_xml_not_camelfood_return_http_500() {
        assertThat(response.getStatus(), is(500));
    }

    @HttpTest( method = Method.POST, path = "/food/eat", file = "/CamelFood.xml")
    public void posting_well_formed_camelfood_return_http_200() {
        assertThat(response.getStatus(), is(200));
    }
}
