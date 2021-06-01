import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Scanner;

public class Producer {
    public static void main(String[] args) throws IOException {
        Properties props = loadProps("/config/setup.properties");
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "console-producer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        final KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Input records following the convention: 'key,value'.");

        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line == "quit") {
                scanner.close();
            } else {
                String[] tokens = line.split(",");
                String key = tokens[0];
                String value = tokens[1];
                String topic = System.getenv("TOPIC");
                ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, key, value);
                producer.send(record);
            }
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down the producer.");
            producer.close();
        }));
    }
    private static Properties loadProps(String file) throws IOException {
        if (!Files.exists(Paths.get(file))) {
            throw new IOException(file + " does not exist or could not be found.");
        }
        final Properties props = new Properties();
        try (InputStream inputStream = new FileInputStream(file)) {
            props.load(inputStream);
        }
        return props;
    }
}
