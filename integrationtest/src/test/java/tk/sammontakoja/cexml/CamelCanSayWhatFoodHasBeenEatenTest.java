package tk.sammontakoja.cexml;

import com.eclipsesource.restfuse.Destination;
import com.eclipsesource.restfuse.HttpJUnitRunner;
import com.eclipsesource.restfuse.Method;
import com.eclipsesource.restfuse.Response;
import com.eclipsesource.restfuse.annotation.Context;
import com.eclipsesource.restfuse.annotation.HttpTest;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.containsString;
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

// TODO Cannot run @Before before @HttpTest, replace com.eclipsesource.restfuse with another tool

@RunWith( HttpJUnitRunner.class )
public class CamelCanSayWhatFoodHasBeenEatenTest {

    @Rule
    public Destination destination = new Destination(this, "http://localhost:8080/camel");

    @Context
    private Response response;

    // To get this pass CamelFood.xml must be posted to http://localhost:8080/camel/food/eat
    @HttpTest( method = Method.GET, path = "/food/eaten")
    public void verify_given_camel_food_is_eaten() {
        assertThat(response.getBody(), containsString("hay"));
        assertThat(response.getBody(), containsString("666"));
    }
}
