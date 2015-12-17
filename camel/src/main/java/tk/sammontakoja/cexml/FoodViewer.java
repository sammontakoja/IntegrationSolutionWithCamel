package tk.sammontakoja.cexml;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author Ari Aaltonen
 */
@Component("foodViewer")
public class FoodViewer {

    @Resource
    public JdbcTemplate jdbcTemplate;

    public void doView(Exchange exchange) {

        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * FROM CAMELFOOD");

        Camelfoodlist camelfoodlist = new ObjectFactory().createCamelfoodlist();
        camelfoodlist.camelfood = new ArrayList<Camelfood>();

        for (Map<String, Object> row : rows)
            camelfoodlist.camelfood.add(map(row));

        exchange.getOut().setBody(camelfoodlist);
    }

    private Camelfood map(Map<String, Object> row) {
        Camelfood camelfood = new ObjectFactory().createCamelfood();
        camelfood.setName((String) row.get("NAME"));
        camelfood.setAmount((Integer) row.get("AMOUNT"));
        return camelfood;
    }
}


