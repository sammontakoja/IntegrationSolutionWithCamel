package tk.sammontakoja.cexml;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.apache.activemq.store.leveldb.LevelDBPersistenceAdapter;
import org.apache.camel.Exchange;
import org.apache.camel.InvalidPayloadException;
import org.apache.camel.Processor;
import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.spring.boot.FatJarRouter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.ConnectionFactory;
import javax.xml.bind.UnmarshalException;

import static org.apache.camel.Exchange.CONTENT_TYPE;
import static org.apache.camel.Exchange.HTTP_RESPONSE_CODE;

@SpringBootApplication
@EnableTransactionManagement
@RestController
public class Application extends FatJarRouter {

    @Override
    public void configure() {

        onException(InvalidPayloadException.class)
                .handled(true)
                .setHeader(HTTP_RESPONSE_CODE, constant(404))
                .setHeader(CONTENT_TYPE, constant("text/plain"))
                .setBody(exceptionMessage());

        onException(UnmarshalException.class)
                .handled(true)
                .setHeader(HTTP_RESPONSE_CODE, constant(404))
                .setHeader(CONTENT_TYPE, constant("text/plain"))
                .setBody(exceptionMessage());

        onException(CannotEatApplesException.class)
                .handled(true)
                .setHeader(HTTP_RESPONSE_CODE, constant(404))
                .setHeader(CONTENT_TYPE, constant("text/plain"))
                .setBody(exceptionMessage())
                .markRollbackOnlyLast();

        restConfiguration().component("servlet").contextPath("/camel").bindingMode(RestBindingMode.auto);

        rest("/eat").post()
                .type(Camelfood.class)
                .to("direct:foodPipe");

        from("direct:foodPipe")
                .transacted("")
                .to("bean:foodConsumer")
                .to("bean:digestion")
                .to("activemq:testingQueue");

        // TODO How to end transaction when msg has been written to queue

        from("activemq:testingQueue").process(new Processor() {
            public void process(Exchange exchange) throws Exception {
                System.out.println("JEE!");
                System.out.println(exchange.getIn());
            }
        });

        rest("/eaten").get().type(String.class).to("bean:foodViewer").outType(Camelfoodlist.class);
    }

    @Bean
    public ServletRegistrationBean camelServletRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new CamelHttpTransportServlet(), "/food/*");
        registration.setName("CamelServlet");
        return registration;
    }

    @Value("${spring.activemq.broker-url}")
    private String activeMQBrokerUrl;

    @Value("${activemq.data.directory}")
    private String activeMQDataDirectory;

    @Bean
    ConnectionFactory jmsConnectionFactory() {
        PooledConnectionFactory pool = new PooledConnectionFactory();
        pool.setConnectionFactory(new ActiveMQConnectionFactory(activeMQBrokerUrl));
        return pool;
    }

    @Bean
    LevelDBPersistenceAdapter myLevelDBPersistenceAdapter() {
        LevelDBPersistenceAdapter levelDBPersistenceAdapter = new LevelDBPersistenceAdapter();
        return levelDBPersistenceAdapter;
    }

    @Bean
    BrokerService myBrokerService(LevelDBPersistenceAdapter levelDBPersistenceAdapter) throws Exception {
        BrokerService broker = new BrokerService();
        broker.setPersistenceAdapter(levelDBPersistenceAdapter);
        broker.setDataDirectory(activeMQDataDirectory);
        broker.setUseShutdownHook(true);
        broker.addConnector(activeMQBrokerUrl);
        return broker;
    }

    @RequestMapping("/services")
    public String home() {
        return "Spring";
    }

}
