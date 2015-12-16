package tk.sammontakoja.cexml;

import org.apache.camel.Body;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Random;

/**
 * @author Ari Aaltonen
 */
@Component("store")
public class Store {

    @Resource
    public JdbcTemplate jdbcTemplate;

    public void doStore(@Body Camelfood camelfood) {
        int key = new Random().nextInt(Integer.MAX_VALUE);
        String sql = "INSERT INTO CAMELFOOD VALUES("+key+", '"+camelfood.getName()+"', "+camelfood.getAmount()+");";
        jdbcTemplate.execute(sql);
    }
}


