# How to use the Tencent plugin

Send notifications and execution alerts to Tencent QQ from Kestra flows.

## Authentication

All tasks require `url` (the QQ incoming webhook URL, required). Optionally set `token` (a verification token for the webhook endpoint). Optionally set `options` for HTTP client configuration (connect timeout, read idle timeout, custom headers). Store secrets in [secrets](https://kestra.io/docs/concepts/secret) and set connection properties on each task.

## Tasks

`qq.QQIncomingWebhook` sends a message as a step in a flow — set `url` and `payload` (both required). Use this for ad-hoc notifications with a custom message body.

`qq.QQExecution` sends a structured execution summary — set `url` and `recipientIds` (the list of QQ user IDs to message, required in practice). It is designed for use with a [Flow trigger](https://kestra.io/docs/workflow-components/triggers) in a dedicated monitoring namespace that reacts to failures in other namespaces. Optionally set `customMessage` and `customFields` to augment the notification, or override `executionId` (default `{{ execution.id }}`).
