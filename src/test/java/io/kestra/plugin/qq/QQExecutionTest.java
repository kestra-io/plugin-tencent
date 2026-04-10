package io.kestra.plugin.qq;

import io.kestra.core.junit.annotations.KestraTest;
import io.kestra.core.repositories.LocalFlowRepositoryLoader;
import io.kestra.core.runners.TestRunner;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@KestraTest
public class QQExecutionTest extends AbstractQQTest {

    @Inject
    protected TestRunner runner;

    @Inject
    protected LocalFlowRepositoryLoader repositoryLoader;

    @BeforeAll
    protected void init() throws IOException, URISyntaxException {
        repositoryLoader.load(Objects.requireNonNull(QQExecutionTest.class.getClassLoader().getResource("flows")));
        this.runner.run();
    }

    @Test
    void flow() throws Exception {
        var failedExecution = runAndCaptureExecution(
            "main-flow-that-fails",
            "tencent-qq"
        );

        String receivedData = waitForWebhookData(
            () -> FakeWebhookController.data != null && FakeWebhookController.data.contains(failedExecution.getId()) ? FakeWebhookController.data : null,
            5000
        );

        assertThat(receivedData, containsString(failedExecution.getId()));
        assertThat(receivedData, containsString("https://mysuperhost.com/kestra/ui"));
        assertThat(receivedData, containsString("Failed Task: failed"));
        assertThat(receivedData, containsString("Final task ID: failed"));
        assertThat(receivedData, containsString("Kestra Tencent QQ notification"));
    }
}
