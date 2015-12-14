package tk.sammontakoja.cexml;

import org.apache.camel.spring.boot.FatJarRouter;
import org.apache.camel.spring.boot.FatWarInitializer;
import org.springframework.web.WebApplicationInitializer;

public class RouterInitializer extends FatWarInitializer implements WebApplicationInitializer {

    @Override
    protected Class<? extends FatJarRouter> routerClass() {
        return Application.class;
    }

}
