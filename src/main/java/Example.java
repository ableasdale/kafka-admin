import org.apache.kafka.clients.admin.ConsumerGroupListing;
import org.apache.kafka.clients.admin.ListConsumerGroupOffsetsResult;
import org.apache.kafka.clients.admin.ListConsumerGroupsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Example {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    public static void main(String[] args) {
        LOG.info("Creating source topic: "+"test");
        NewTopic sourceTopic = new NewTopic("test", 1, (short) 1);
        Helper.getAdminClient().createTopics(Collections.singleton(sourceTopic));

        LOG.info("Creating destination topic: "+"test_dest");
        // We're creating this with a small max.message.bytes to trigger an exception when Kafka Streams runs..
        Map<String, String> configMap = new HashMap<>();
        //configMap.put("cleanup.policy", "compact");
        configMap.put(TopicConfig.MAX_MESSAGE_BYTES_CONFIG, "96");
        NewTopic destinationTopic = new NewTopic("test_dest", 1, (short) 1)
                .configs(configMap);
        Helper.getAdminClient().createTopics(Collections.singleton(destinationTopic));

        LOG.info("Listing Available Topics:");
        try {
            LOG.info("******************");
            Helper.getAdminClient().listTopics().names().get().forEach(LOG::info);
            LOG.info("******************");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }


        /*
        List Consumer Groups
        default ListConsumerGroupsResult listConsumerGroups()
List the consumer groups available in the cluster with the default options.
This is a convenience method for listConsumerGroups(ListConsumerGroupsOptions) with default options. See the overload for more details.

Returns:
The ListGroupsResult.
         */
        LOG.info("Listing Consumer Groups:");
        try {
            LOG.info("******************");
            ListConsumerGroupsResult lgr = Helper.getAdminClient().listConsumerGroups(); //.names().get().forEach(LOG::info);
            for (ConsumerGroupListing l : lgr.all().get()){
                LOG.info(l.toString());
                LOG.info(l.groupId());
            }
            LOG.info("******************");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
