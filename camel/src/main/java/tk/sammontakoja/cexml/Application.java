package tk.sammontakoja.cexml;

import org.apache.camel.InvalidPayloadException;
import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.spring.boot.FatJarRouter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.UnmarshalException;

import static org.apache.camel.Exchange.*;

@SpringBootApplication
@EnableTransactionManagement
@RestController
public class Application extends FatJarRouter {

    @Override
    public void configure() {

        onException(InvalidPayloadException.class)
                .handled(true)
                .setHeader(HTTP_RESPONSE_CODE, constant(404))
                .setHeader(CONTENT_TYPE, constant("text/plain"))
                .setBody(exceptionMessage());

        onException(UnmarshalException.class)
                .handled(true)
                .setHeader(HTTP_RESPONSE_CODE, constant(404))
                .setHeader(CONTENT_TYPE, constant("text/plain"))
                .setBody(exceptionMessage());

        onException(CannotEatApplesException.class)
                .handled(true)
                .setHeader(HTTP_RESPONSE_CODE, constant(404))
                .setHeader(CONTENT_TYPE, constant("text/plain"))
                .setBody(exceptionMessage())
                .markRollbackOnlyLast();

        restConfiguration().component("servlet").contextPath("/camel").bindingMode(RestBindingMode.auto);

        rest("/eat").post()
                .type(Camelfood.class)
                .to("direct:foodPipe");

        from("direct:foodPipe")
                .transacted()
                .to("bean:foodConsumer")
                .to("bean:digestion");

        rest("/eaten").get().type(String.class).to("bean:foodViewer").outType(Camelfoodlist.class);
    }

    @Bean
    public ServletRegistrationBean camelServletRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new CamelHttpTransportServlet(), "/food/*");
        registration.setName("CamelServlet");
        return registration;
    }

    @RequestMapping("/services")
    public String home() {
        return "Spring";
    }

}
