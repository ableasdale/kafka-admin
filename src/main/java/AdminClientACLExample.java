import org.apache.kafka.clients.admin.DescribeAclsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.acl.*;
import org.apache.kafka.common.resource.PatternType;
import org.apache.kafka.common.resource.ResourcePattern;
import org.apache.kafka.common.resource.ResourcePatternFilter;
import org.apache.kafka.common.resource.ResourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

public class AdminClientACLExample {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static void main(String[] args) {
        // 1. Create 100 topics (
        Collection<NewTopic> topics = new ArrayList<NewTopic>();

        for (int i = 1; i <= 100; i++) {
            topics.add(new NewTopic("test-" + i, 1, (short) 1));
        }
        Helper.getAdminClient().createTopics(topics).all();

        // 2. List topics
        LOG.info("Listing Available Topics:");
        try {
            Helper.getAdminClient().listTopics().names().get().forEach(LOG::info);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        // 3. Create ACL Bindings
        Collection<AclBinding> aclBindings = new ArrayList<AclBinding>();

        for (int i = 1; i <= 100; i++) {
            aclBindings.add(new AclBinding(new ResourcePattern(ResourceType.TOPIC, "test-" + i, PatternType.LITERAL),
                    new AccessControlEntry("User:alex", "*", AclOperation.WRITE, AclPermissionType.DENY))
            );
            aclBindings.add(new AclBinding(new ResourcePattern(ResourceType.TOPIC, "test-" + i, PatternType.LITERAL),
                    new AccessControlEntry("User:*", "*", AclOperation.DESCRIBE, AclPermissionType.ALLOW))
            );
        }
        Helper.getAdminClient().createAcls(aclBindings).all();

        // 4. List ACLs
        try {

            DescribeAclsResult dar = Helper.getAdminClient().describeAcls(new AclBindingFilter(ResourcePatternFilter.ANY, AccessControlEntryFilter.ANY));

            Collection<AclBinding> acls = dar.values().get();

            for (AclBinding ab : acls) {
                LOG.info("ACL: " + ab.toString());
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }
}
