package tk.sammontakoja.cexml;

import org.apache.camel.Body;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @author Ari Aaltonen
 */
@Component("lawn")
public class Lawn {

    private static Logger LOG = LoggerFactory.getLogger(Lawn.class);

    public void pooOnTheLawn(@Body Camelfood camelfood) {

        int pooTime = new Random().nextInt(3000);
        try {
            Thread.sleep(pooTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        LOG.debug("Poo found on the lawn!! '{}'", camelfood);
    }

}


