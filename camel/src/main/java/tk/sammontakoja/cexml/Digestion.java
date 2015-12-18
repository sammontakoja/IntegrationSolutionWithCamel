package tk.sammontakoja.cexml;

import org.apache.camel.Body;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @author Ari Aaltonen
 */
@Component("digestion")
public class Digestion {

    private static Logger LOG = LoggerFactory.getLogger(Digestion.class);

    public void digest(@Body Camelfood camelfood) {

        String foodName = camelfood.getName().toLowerCase();

        if (properFood(foodName))
            digest(foodName);
        else
            throw new RuntimeException("Cannot eat " + foodName);
    }

    private boolean properFood(String food) {
        return (food.contains("hay") || food.contains("dry grain"));
    }

    private void digest(String camelfood) {
        LOG.debug("Started to digest '{}'", camelfood);
        int digestionTime = new Random().nextInt(3000);
        try {
            Thread.sleep(digestionTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        LOG.debug("Waste done '{}'", camelfood);
    }

}


