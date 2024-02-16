import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;


public class Send {
    private final static String QUEUE_NAME = "senzoriii";
    private static String DEVICE_ID;

    public static void main(String[] argv) throws Exception {

        DEVICE_ID = argv[0];

        String desktopPath = System.getProperty("user.home") + "/Desktop/";
        String sensorFileName = "sensor.csv";
        String sensorFilePath = desktopPath + sensorFileName;

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqps://qqrckpte:XJaGGNrsmSydPVpYwZishlWLKf48xUYf@moose.rmq.cloudamqp.com/qqrckpte");

        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);

            // Trimite linii din "sensor.csv"
            try (BufferedReader br = new BufferedReader(new FileReader(sensorFilePath))) {
                String line;
                String deviceId = DEVICE_ID;

                while ((line = br.readLine()) != null) {
                    long timestamp = System.currentTimeMillis();
                    String json = createJson(line, timestamp, deviceId);
                    System.out.println("timestamp: " + timestamp + ", device_id: " + deviceId + ", measurement_value: " + line);

                    channel.basicPublish("", QUEUE_NAME, null, json.getBytes(StandardCharsets.UTF_8));
                    Thread.sleep(24000);
                }
            }
        }
    }

    private static String createJson(String line, long timestamp, String deviceId) {
        return "{\"data\": \"" + line + "\", \"timestamp\": " + timestamp + ", \"device_id\": \"" + deviceId + "\"}";
    }

}