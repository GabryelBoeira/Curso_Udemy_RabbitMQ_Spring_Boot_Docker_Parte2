package com.br.gabryel.spring.consumer;

import com.br.gabryel.spring.consumer.amqp.impl.RabbitMQConsumerImpl;
import com.br.gabryel.spring.consumer.model.Mensagem;
import com.google.common.io.Files;
import org.apache.qpid.server.BrokerOptions;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.ExternalResource;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.apache.qpid.server.Broker;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringConsumerRabbitMQTest {

    @Value("${spring.rabbitmq.port}")
    private String rabbitmqPort;

    @Value("${spring.rabbitmq.request.exchange.producer}")
    private String exchange;

    @Value("${spring.rabbitmq.request.routing-key.producer}")
    private String queue;

    public static final String QPID_CONFIG_LOCATION = "src/test/resources/qpid-config.json";
    public static final String APPLICATION_CONFIG_LOCATION = "src/test/resources/application.yml";

    @MockBean
    private Runner runner;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQConsumerImpl consumer;

    @ClassRule
    public static final ExternalResource resource = new ExternalResource() {
        private Broker broker = new Broker();

        @Override
        protected void before() throws Throwable {
            Properties properties = new Properties();
            properties.load(new FileInputStream(new File(APPLICATION_CONFIG_LOCATION)));
            String amqpPort = properties.getProperty("port");
            File tmpFolder = Files.createTempDir();
            String userDir = System.getProperty("user.dir").toString();
            File file = new File(userDir);
            String homePath = file.getAbsolutePath();
            BrokerOptions brokerOptions = new BrokerOptions();
            brokerOptions.setConfigProperty("qpid.work_dir", tmpFolder.getAbsolutePath());
            brokerOptions.setConfigProperty("qpid.amqp_port", amqpPort);
            brokerOptions.setConfigProperty("qpid.home_dir", homePath);
            brokerOptions.setInitialConfigurationLocation(homePath + "/" + QPID_CONFIG_LOCATION);
            broker.startup(brokerOptions);
        }

        @Override
        protected void after() {
            broker.shutdown();
        }
    };

    @Test
    public void testWithFirstReceiverRoutingKey() throws Exception {
        consumer.initCounter();
        rabbitTemplate.convertAndSend(exchange, queue, new Mensagem("Hello from RabbitMQ Sent 1!"));
        rabbitTemplate.convertAndSend(exchange, queue, new Mensagem("Hello from RabbitMQ Sent 2!"));
        rabbitTemplate.convertAndSend(exchange, queue, new Mensagem("Hello from RabbitMQ Sent 3!"));
        Thread.sleep(5000);
        assertThat(consumer.getCounter()).isEqualTo(3);
    }
}
