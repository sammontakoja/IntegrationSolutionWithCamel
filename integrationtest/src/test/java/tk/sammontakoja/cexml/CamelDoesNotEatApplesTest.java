package tk.sammontakoja.cexml;

import com.eclipsesource.restfuse.Destination;
import com.eclipsesource.restfuse.HttpJUnitRunner;
import com.eclipsesource.restfuse.Method;
import com.eclipsesource.restfuse.Response;
import com.eclipsesource.restfuse.annotation.Context;
import com.eclipsesource.restfuse.annotation.HttpTest;
import org.junit.Rule;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Ari Aaltonen
 */

@RunWith( HttpJUnitRunner.class )
public class CamelDoesNotEatApplesTest {

    @Rule
    public Destination destination = new Destination(this, "http://localhost:8080/camel");

    @Context
    private Response response;

    @HttpTest( method = Method.POST, path = "/food/eat", file = "/CamelFoodAsApples.xml", order = 1)
    public void when_given_apple_camel_return_http_404() {
        System.out.println(response.getBody());
        assertThat(response.getStatus(), is(404));
    }

    @HttpTest( method = Method.GET, path = "/food/eaten", order = 2)
    public void making_sure_apple_is_not_eaten() {
        assertThat(response.getStatus(), is(200));
        assertThat(response.getBody(), not(containsString("apples")));
    }

}
