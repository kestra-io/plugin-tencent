package io.kestra.plugin.qq;


import com.google.common.io.Files;
import io.kestra.core.junit.annotations.KestraTest;
import io.kestra.core.models.property.Property;
import io.kestra.core.runners.RunContext;
import io.kestra.core.runners.RunContextFactory;
import io.kestra.plugin.tencent.qq.QQIncomingWebhook;
import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.server.EmbeddedServer;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

@KestraTest
public class QQIncomingWebhookTest {

    @Inject
    private ApplicationContext applicationContext;

    @Inject
    private RunContextFactory runContextFactory;

    @Test
    void run() throws Exception {
        RunContext runContext = runContextFactory.of(Map.of(
            "text", "Tencent QQ notification"
        ));

        EmbeddedServer embeddedServer = applicationContext.getBean(EmbeddedServer.class);
        embeddedServer.start();

        QQIncomingWebhook task = QQIncomingWebhook.builder()
            .url(Property.ofValue(embeddedServer.getURI() + "/webhook-unit-test"))
            .payload(Property.ofValue(
                Files.asCharSource(
                    new File(Objects.requireNonNull(QQIncomingWebhookTest.class.getClassLoader()
                            .getResource("tencent-qq-test.peb"))
                        .toURI()),
                    StandardCharsets.UTF_8
                ).read()
                    ))
            .build();

        task.run(runContext);

        assertThat(FakeWebhookController.data, containsString("Tencent QQ notification"));
    }

}
