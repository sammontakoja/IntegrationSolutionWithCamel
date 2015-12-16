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
        String sql = "SELECT * FROM CAMELFOOD";

        StringBuilder sb = new StringBuilder();

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> row : rows) {
            sb.append("\n");
            sb.append(row.get("ID"));
            sb.append(row.get("NAME"));
            sb.append(row.get("AMOUNT"));
        }

        exchange.getOut().setBody(sb.toString());
    }
}


