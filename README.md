# SentinelCore

SentinelCore is an advanced, enterprise-grade open-source security and anti-exploit plugin designed specifically for Minecraft servers running Paper 1.21.11+. Developed with performance and robustness in mind by altblack.

---

## Features

1. Anti-Exploit & Anti-Crash Protection: Filters malicious payload injections, blocks oversized book exploit packets, and sanitizes input data.
2. Crash Watchdog: Hooks into Java's uncaught exception handler and critical error pathways to instantly dispatch detailed stack traces and diagnostics via Discord Webhook.
3. Advanced Rate-Limiting: Intelligently throttles command execution rates with customizable thresholds, warnings, and automated punitive actions (kick/ban).

---

## Installation

1. Download the latest release JAR from GitHub or build from source using Maven.
2. Place SentinelCore.jar into your server's plugins/ directory.
3. Start or restart your server to generate default configurations.
4. Configure your Discord Webhook URL and rate-limiting rules in plugins/SentinelCore/config.yml.

---

## Configuration (config.yaml)

discord:
  enabled: true
  webhook-url: "YOUR_DISCORD_WEBHOOK_URL"
  bot-name: "SentinelCore Watchdog"

security:
  max-command-length: 256
  command-rate-limit:
    enabled: true
    cooldown-ms: 500
    max-warnings: 3
    action: "kick"
  exploit-filter:
    block-oversized-books: true
    max-book-page-length: 1024

---

## Commands & Permissions

- /sentinelcore - Displays plugin status.
- /sentinelcore reload - Reloads configuration files (sentinelcore.admin).
- sentinelcore.bypass - Permission node to bypass rate limiting and exploit checks.

---

## License


Maintained with ❤️ by altblack.
