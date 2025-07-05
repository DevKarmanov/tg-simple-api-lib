# tg-simple-api

[![License](https://img.shields.io/github/license/DevKarmanov/tg-simple-api-lib)](LICENSE)

`tg-simple-api` — это Java-библиотека с интеграцией Spring, предназначенная для быстрого и удобного создания Telegram-ботов с помощью аннотаций и встроенного менеджера состояний.

## 🧩 Плагин IntelliJ IDEA для удобства разработки

Для более комфортной работы с `tg-simple-api` рекомендую установить официальный [плагин](https://github.com/DevKarmanov/tg-simple-api-intellij-plugin) для IntelliJ IDEA.

- Плагин автоматически распознает аннотации `tg-simple-api`.
- Подавляет лишние предупреждения IDE.
- Планируется расширение новыми полезными функциями.

Скачать последние релизы и узнать подробности можно здесь:  
[https://github.com/DevKarmanov/tg-simple-api-intellij-plugin/releases/latest](https://github.com/DevKarmanov/tg-simple-api-intellij-plugin/releases/latest)


## 📦 Подключение

[![](https://jitpack.io/v/DevKarmanov/tg-simple-api-lib.svg)](https://jitpack.io/#DevKarmanov/tg-simple-api-lib)

## 🚀 Быстрый старт

1. Создайте бин для `BotHandler` и вашего бота:
    

```java
@Configuration
@EnableConfigurationProperties(BotProperties.class)
public class BotConfig {
    private final BotProperties botProperties;

    public BotConfig(BotProperties botProperties) {
        this.botProperties = botProperties;
    }

    @Bean
    public TelegramBot telegramBot() {
        return new TelegramBot(botProperties.token(), botProperties.name(), botProperties.webHookPath());
    }

    @Bean
    public BotHandler botHandler() {
        return new DefaultBotHandler();
    }
}
```

2. Передайте `Update` в `BotHandler`:
    

```java
@RestController
public class WebHookController {
    private final BotHandler botHandler;

    public WebHookController(BotHandler botHandler) {
        this.botHandler = botHandler;
    }

    @PostMapping("/webhook")
    public ResponseEntity<?> receiveWebhook(@RequestBody Update update) {
        botHandler.handleMessage(update);
        return ResponseEntity.ok().build();
    }
}
```

