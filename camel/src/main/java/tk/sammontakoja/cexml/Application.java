package tk.sammontakoja.cexml;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.apache.activemq.store.leveldb.LevelDBPersistenceAdapter;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.InvalidPayloadException;
import org.apache.camel.Processor;
import org.apache.camel.component.jms.JmsConfiguration;
import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.spring.boot.FatJarRouter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.UnmarshalException;

import java.io.File;

import static org.apache.camel.Exchange.CONTENT_TYPE;
import static org.apache.camel.Exchange.HTTP_RESPONSE_CODE;

@SpringBootApplication
@EnableJms
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
                .transacted()
                .to("foodConsumer")
                .to("digestion")
                .to(ExchangePattern.InOnly, "activemq:testingQueue");

        from("activemq:testingQueue")
                .to("lawn");

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

    @Bean(name = "pooledConnectionFactory")
    public PooledConnectionFactory getPooledConnectionFactory() {

        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(activeMQBrokerUrl);

        PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
        pooledConnectionFactory.setMaxConnections(8);
        pooledConnectionFactory.setConnectionFactory(activeMQConnectionFactory);
        return pooledConnectionFactory;
    }

    @Bean(name = "jmsConfig")
    JmsConfiguration getJmsConfiguration() {
        JmsConfiguration jmsConfiguration = new JmsConfiguration();
        jmsConfiguration.setConnectionFactory(getPooledConnectionFactory());
        return jmsConfiguration;
    }

    @Bean(name = "activemq")
    ActiveMQComponent getActiveMQComponent(JmsConfiguration jmsConfiguration) {
        ActiveMQComponent activeMQComponent = new ActiveMQComponent();
        activeMQComponent.setConfiguration(jmsConfiguration);
        activeMQComponent.setTransacted(false);
        activeMQComponent.setCacheLevel(0);
        return activeMQComponent;
    }

    @Bean
    LevelDBPersistenceAdapter myLevelDBPersistenceAdapter() {
        LevelDBPersistenceAdapter levelDBPersistenceAdapter = new LevelDBPersistenceAdapter();
        levelDBPersistenceAdapter.setDirectory(new File(activeMQDataDirectory));
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
