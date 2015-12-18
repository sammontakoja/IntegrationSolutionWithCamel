package tk.sammontakoja.cexml;

import com.eclipsesource.restfuse.Destination;
import com.eclipsesource.restfuse.HttpJUnitRunner;
import com.eclipsesource.restfuse.Method;
import com.eclipsesource.restfuse.Response;
import com.eclipsesource.restfuse.annotation.Context;
import com.eclipsesource.restfuse.annotation.HttpTest;
import org.junit.Rule;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Ari Aaltonen
 */

@RunWith( HttpJUnitRunner.class )
public class CamelDoesNotEatNonXmlContentTest {

    @Rule
    public Destination destination = new Destination(this, "http://localhost:8080/camel");

    @Context
    private Response response;

    @HttpTest( method = Method.POST, path = "/food/eat", file = "/foo.txt", order = 1)
    public void posting_xml_not_camelfood_return_http_404() {
        System.out.println(response.getBody());
        assertThat(response.getStatus(), is(404));
    }

    @HttpTest( method = Method.GET, path = "/food/eaten", order = 2)
    public void vefrify_foo_is_not_eaten() {
        assertThat(response.getStatus(), is(200));
        assertThat(response.getBody(), not(containsString("bar")));
    }

}
