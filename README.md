# Java Console Producer

### **Things you'll need before getting started:**

1. You'll need a cluster running in Confluent Cloud with a topic to send data too. In this `README`, I'll be referencing a topic named `colors`. 
1. You'll need to copy the client configuration properties from the **Clients** page in your cluster. This should give you a copy-able set of properties that you'll to use.
    - You should paste the specific values into the placeholders seen in `setup.properties`, or alternatively paste over the entire thing.
    
### **Using the Console Producer**

- Either build the image yourself using the provided `Dockerfile`, or reference the pre-built image `zachhamilton/console-producer`. This `README` will use the latter. 

- Reference the following table to see the configuration parameters that can be passed to the producer as environment variables.
  
  | Environment Variable | Default Value        | Required? |
  |----------------------|----------------------|:---------:|
  | `TOPIC`              | n/a                  | Y         |

- You'll need to volume mount the producer container to the directory where `setup.properties` is found in order for the producer to use the configuration.
    ```bash
    ...
    -v $(pwd)/:/config/:ro
    ...
    ```
- The following is an example of starting the console producer with the topic name  `colors`. 
    ```bash
    docker run --rm -it --name console-producer \
    -e TOPIC="colors" \
    -v $(pwd)/:/config/:ro \
    zachhamilton/console-producer
    ```
    Assuming everything worked correctly, the command won't return and the cursor will hang awaiting a user input. 
- You can create new messages by simply adding a `key` and `value` separated by **single** comma and pressing enter. The message will be sent and the cursor will move on to the next line awaiting another input. The follow is an example of the format for creating new messages.
    ```bash
    ...
    key,value

    blue,my favorite color
    ...
    ```
    In the above, the first message was `"key,value"` and the second was `"blue,my favorite color"`. The string will get split by the comma (which is why you can only use a **single** comma) and use the first segment as the key and the second as the value. 
    
    In the above, the `keys` were **key** and **blue** and the `values` were **value** and **my favorite color**. 