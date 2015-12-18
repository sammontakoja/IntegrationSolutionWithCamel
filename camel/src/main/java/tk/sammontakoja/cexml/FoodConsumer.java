package tk.sammontakoja.cexml;

import org.apache.camel.Body;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Random;

/**
 * @author Ari Aaltonen
 */
@Component("foodConsumer")
public class FoodConsumer {

    private static Logger LOG = LoggerFactory.getLogger(FoodConsumer.class);

    @Resource
    public JdbcTemplate jdbcTemplate;

    public void consumeFood(@Body Camelfood camelfood) {
        LOG.debug("To DB '{}'", camelfood);
        int key = new Random().nextInt(Integer.MAX_VALUE);
        String sql = "INSERT INTO CAMELFOOD VALUES("+key+", '"+camelfood.getName()+"', "+camelfood.getAmount()+");";
        jdbcTemplate.execute(sql);
    }
}


