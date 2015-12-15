package tk.sammontakoja.cexml;

import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.apache.camel.spring.boot.FatJarRouter;
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
        restConfiguration().component("servlet");
        rest("/foo").get("/bar").route().transform().simple("Rest");
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new CamelHttpTransportServlet(), "/food/*");
        registration.setName("CamelServlet");
        return registration;
    }

    @RequestMapping("/services")
    public String home() {
        return "Spring";
    }

}
