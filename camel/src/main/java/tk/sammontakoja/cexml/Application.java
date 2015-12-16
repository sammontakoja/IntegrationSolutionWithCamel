package tk.sammontakoja.cexml;

import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.spring.boot.FatJarRouter;
import org.h2.server.web.WebServlet;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application extends FatJarRouter {

    @Override
    public void configure() {

        restConfiguration().component("servlet").contextPath("/camel").bindingMode(RestBindingMode.xml);

        rest("/eat").post().type(Camelfood.class).to("bean:store");
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
