package tk.sammontakoja.cexml;

import org.apache.camel.Body;
import org.springframework.stereotype.Component;

/**
 * @author Ari Aaltonen
 */
@Component("store")
public class Store {

    public void doStore(@Body Camelfood camelfood) {
        System.out.println("Storing camelfood");
        System.out.println(camelfood.getName() + " " + camelfood.getAmount());
    }

}
