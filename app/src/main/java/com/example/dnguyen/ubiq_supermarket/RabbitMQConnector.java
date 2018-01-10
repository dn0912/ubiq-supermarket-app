package com.example.dnguyen.ubiq_supermarket;

import android.util.Log;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by dnguyen on 10.01.18.
 */

public class RabbitMQConnector {
    private ConnectionFactory factory;
    private Connection conn;
    private Channel channel;

    private static final String EXCHANGE_NAME = "supermarkt_duc";

    public RabbitMQConnector() {
        factory = new ConnectionFactory();
        factory.setUsername("master");
        factory.setPassword("master");
        factory.setHost("155.54.204.46");
        factory.setPort(5672);
        try {
            conn = factory.newConnection();
            channel = conn.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, "topic", true);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    public void publishMessage(String routingKey, String msgContent) {
        try {
            this.channel.basicPublish(EXCHANGE_NAME, routingKey, null, msgContent.getBytes());
            Log.e("rabbitmq", "routingkey: " + routingKey + " message: " + msgContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
