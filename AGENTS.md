# Kestra Tencent Plugin

## What

- Provides plugin components under `io.kestra.plugin.tencent`.
- Includes classes such as `QQTemplate`, `QQIncomingWebhook`, `QQExecution`.

## Why

- This plugin integrates Kestra with Tencent.
- It provides tencent integrations for Kestra.

## How

### Architecture

Single-module plugin. Source packages under `io.kestra.plugin`:

- `templates`

Infrastructure dependencies (Docker Compose services):

- `app`

### Key Plugin Classes

- `io.kestra.plugin.templates.Example`

### Project Structure

```
plugin-tencent/
├── src/main/java/io/kestra/plugin/templates/
├── src/test/java/io/kestra/plugin/templates/
├── build.gradle
└── README.md
```

## References

- https://kestra.io/docs/plugin-developer-guide
- https://kestra.io/docs/plugin-developer-guide/contribution-guidelines
