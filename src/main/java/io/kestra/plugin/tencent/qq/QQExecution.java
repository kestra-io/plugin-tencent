package io.kestra.plugin.tencent.qq;

import io.kestra.core.models.annotations.Example;
import io.kestra.core.models.annotations.Plugin;
import io.kestra.core.models.property.Property;
import io.kestra.core.models.tasks.VoidOutput;
import io.kestra.core.plugins.notifications.ExecutionInterface;
import io.kestra.core.plugins.notifications.ExecutionService;

import io.kestra.core.runners.RunContext;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@SuperBuilder
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Schema(
    title = "Send a Tencent QQ notification on a Kestra execution",
    description = "Renders a template with execution context and sends it to the configured QQ recipients."
)
@Plugin(
    examples = {
        @Example(
            title = "Send a Tencent IM / QQ notification on failed executions",
            full = true,
            code = """
                id: failure_alert_qq
                namespace: company.team

                tasks:
                  - id: send_qq_alert
                    type: io.kestra.plugin.qq.QQExecution
                    url: "https://console.tim.qq.com/v4/openim/sendmsg?sdkappid=xxx&identifier=admin&usersig=xxx&random=9999&contenttype=json"
                    recipientIds:
                      - "QQ_USER_ID_1"
                      - "QQ_USER_ID_2"
                    executionId: "{{ trigger.executionId }}"
                    customMessage: "Flow {{ flow.id }} failed"

                triggers:
                  - id: failed_prod
                    type: io.kestra.plugin.core.trigger.Flow
                    conditions:
                      - type: io.kestra.plugin.core.condition.ExecutionStatus
                        in: [FAILED, WARNING]
                """
        )
    }
)
public class QQExecution extends QQTemplate implements ExecutionInterface {

    @Builder.Default
    private final Property<String> executionId = Property.ofExpression("{{ execution.id }}");

    private Property<String> customMessage;

    private Property<Map<String, Object>> customFields;

    @Override
    public VoidOutput run(RunContext runContext) throws Exception {
        this.templateUri = Property.ofValue("tencent-qq.peb");
        this.templateRenderMap = Property.ofValue(ExecutionService.executionMap(runContext, this));
        return super.run(runContext);
    }
}
