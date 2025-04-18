# tg-simple-api

`tg-simple-api` — это Java-библиотека с интеграцией Spring, предназначенная для быстрого и удобного создания Telegram-ботов с помощью аннотаций и встроенного менеджера состояний.

## 📦 Подключение

**Maven**:

```xml
<!-- пока не работает -->
<dependency>
    <groupId>your.group.id</groupId>
    <artifactId>tg-simple-api</artifactId>
    <version>your-version</version>
</dependency>
```

## 🚀 Быстрый старт

1. Добавьте аннотацию `@EnableTgSimpleApi` в ваш основной класс:
    

```java
@EnableTgSimpleApi
@SpringBootApplication
public class BotApp {
    public static void main(String[] args) {
        SpringApplication.run(BotApp.class, args);
    }
}
```

2. Создайте бин для `BotHandler` и вашего бота:
    

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
        return new BotHandler();
    }
}
```

3. Передавайте `Update` в `BotHandler`:
    

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

## 📚 Аннотации

### `@BotText`

Обрабатывает текстовые сообщения:

```java
@BotText(text = "/start")
public void startCommand(Update update) { }
```

- `actionName` — ID действия. Это строка, которая позволяет связать методы и состояние пользователя. По умолчанию у метода с `@BotText("/start")` установлено значение `start-command-method`. Это значение служит стартовой точкой и может быть использовано как ссылка в состоянии. Оно не обязательно уникально: если несколько методов используют одинаковое `actionName`, то они будут доступны в менеджере состояний под одним именем.
    
- `text` — текст сообщения (можно использовать regex, если `isRegex = true`)
    
- `order` — порядок выполнения методов (если методов вызывается несколько)
    
- `isRegex` — использовать ли регулярное выражение
    

**Изменение стартового действия по умолчанию:**

По умолчанию вызывается метод с `@BotText("/start")`. Чтобы задать другой метод, нужно изменить `defaultStartActionName`:

```java
@Service
public class MessageService {
    private final StateManager manager;

    @PostConstruct
    public void init() {
        manager.setDefaultStartActionName("new-default-start-action-name");
    }

    @BotText(actionName = "new-default-start-action-name")
    public void handleStart(Update update) { }
}
```
Стандартное значение нужно для того чтобы указать библиотеке какой метод будет использоваться при первом обращении пользователя к боту.

---
### `@BotCallBack`

Обработка callback-запросов:

```java
@BotCallBack(actionName = "create", callbackName = {"^.*ad$", "^read.*"}, isRegex = true)
public void handleCallback(Update update) { }
```

- **`actionName`** — ID действия.
    
- **`callbackName`** — Массив строк, содержащий регулярные выражения (или имена callback'ов) для callback-данных.
    
- **`isRegex`** — Использовать ли регулярное выражение для сравнения callback.
    
- **`order`** — Порядок выполнения.
---
### `@BotMedia`

Обработка медиафайлов:

```java
@BotMedia(actionName = "media-action", mediaType = MediaType.PHOTO)
public void handleMedia(Update update) { }
```

- **`actionName`** — ID действия.
    
- **`mediaType`** — Тип медиафайла (например, `MediaType.PHOTO`).
    
- **`order`** — Порядок выполнения.
---
### `@BotPhoto`

Более точная обработка изображений с фильтрами:

```java
@BotPhoto(actionName = "photo-action", minWidth = 100, aspectRatio = "1:1")
public void handlePhoto(Update update) { }
```

- **`actionName`** — ID действия.
    
- **`minFileSize`** — Минимальный размер файла (в байтах).
    
- **`maxFileSize`** — Максимальный размер файла (в байтах).
    
- **`minWidth`** — Минимальная ширина изображения.
    
- **`minHeight`** — Минимальная высота изображения.
    
- **`aspectRatio`** — Соотношение сторон изображения.
    
- **`format`** — Формат изображения.
---
### `@BotScheduled`

Аннотация `@BotScheduled` используется для планирования задач, которые будут выполняться по расписанию, с учетом роли пользователя, для которого они актуальны. Вот как это работает:

```java
@BotScheduled(fixedRate = 10000, zone = "Europe/Minsk", runOnStartup = true, roles = {"admin"})
public void scheduledMessage(Set<Long> selectedUserIds) {
    // Логика обработки для пользователей с ролью "admin"
}
```

### Пояснение параметров:

- **`fixedRate`** — Частота, с которой задача будет выполняться в миллисекундах. Например, `10000` означает выполнение задачи каждые 10 секунд.
    
- **`runOnStartup`** — Определяет, будет ли задача выполнена при старте приложения. Если установлено в `true`, задача будет выполнена один раз сразу после запуска приложения.
    
- **`roles`** — Список ролей пользователей, для которых задача будет доступна. В этом примере задача будет выполняться только для пользователей с ролью `"admin"`. Метод будет вызываться для каждого пользователя, чей `userId` соответствует роли, указанной в `roles`.
    
- **`cron`** — CRON-выражение для задания расписания выполнения задачи (вместо `fixedRate` или `fixedDelay`).
    
- **`fixedDelay`** — Задержка между выполнения задачи, заданная в миллисекундах. В отличие от `fixedRate`, задача будет выполнена только после завершения предыдущей.
    
- **`zone`** — Часовой пояс, в котором будет выполняться задача. Например, `"Europe/Minsk"` для Минска.
    
### Как это работает на практике:

1. **Подбор пользователей с подходящей ролью:** Когда задача запланирована с параметром `roles = {"admin"}`, она будет вызываться только для тех пользователей, у которых есть эта роль. Это означает, что метод будет выполняться для каждого пользователя, чей `userId` соответствует роли `"admin"`.
    
2. **Передача списка пользователей:** В метод передается список `selectedUserIds`, который содержит `userId` всех пользователей с ролью `"admin"`. Таким образом, метод может обработать информацию для каждого пользователя в списке.
    
3. **Выполнение задачи в заданное время:** Задача будет выполнена по расписанию (каждые 10 секунд в данном примере), и для каждого пользователя из списка `selectedUserIds` будет выполнена соответствующая логика (например, отправка сообщения пользователю, обновление данных и т.д.).
    
4. **Как происходит отбор пользователей:** В процессе выполнения задачи система сначала отбирает пользователей, у которых есть указанные роли, затем передает их `id` в метод. Это гарантирует, что задача будет выполнена только для тех пользователей, которые соответствуют указанным в аннотации ролям. В этом примере, если пользователь имеет роль `"admin"`, его `userId` попадет в список и будет обработан.
    

---
### `@RoleBasedAccess`

Ограничивает доступ к обработчику по ролям:

```java
@BotCallBack(actionName = "admin-action", callbackName = "admin-callback")
@RoleBasedAccess(roles = {"admin"})
public void adminOnly(Update update) { }
```

- **`roles`** — Роли, которым доступен данный метод. (все пользователи инициализируются как `user` при первом обращении)

---

## 🧠 Менеджер состояний `StateManager`

Компонент `StateManager` управляет текущим состоянием пользователя и позволяет реализовать многошаговые сценарии взаимодействия.

### Методы:

- `setState(Long userId, UserContext context)` — задает состояние пользователю. `UserContext` содержит:
    
    - `UserState` — тип ожидаемого взаимодействия (например, текст, фото, callback)
        
    - список допустимых `actionName` — какие действия разрешены в этом состоянии
        
- `setUserRole(Long userId, String role)` — устанавливает роль для пользователя (например, "admin")
    
- `setDefaultStartActionName(String actionName)` — задает стартовое действие по умолчанию, которое вызывается при первом сообщении пользователя или сбросе состояния
    

### Слушатели:

Менеджер состояний также поддерживает добавление и удаление слушателей для изменений ролей и состояний.

- `addRoleChangeListener(RoleChangeListener roleChangeListener)` — добавляет слушателя для изменений роли пользователя.
    
- `removeRoleChangeListener(RoleChangeListener roleChangeListener)` — удаляет слушателя для изменений роли пользователя.
    
- `addStateChangeListener(StateChangeListener stateChangeListener)` — добавляет слушателя для изменений состояния пользователя.
    
- `removeStateChangeListener(StateChangeListener stateChangeListener)` — удаляет слушателя для изменений состояния пользователя.
    

### Интерфейсы слушателей:

- **`RoleChangeListener`**: интерфейс для прослушивания изменений ролей пользователя.
    
    ```java
    public interface RoleChangeListener {
        void onRoleChange(Long userId, String oldRole, String newRole);
    }
    ```
    
    - `onRoleChange(Long userId, String oldRole, String newRole)` — вызывается при изменении роли пользователя. Параметры:
        
        - `userId` — идентификатор пользователя
            
        - `oldRole` — старая роль пользователя
            
        - `newRole` — новая роль пользователя
            
- **`StateChangeListener`**: интерфейс для прослушивания изменений состояний пользователя.
    
    ```java
    public interface StateChangeListener {
        void onStateChange(Long userId, UserContext oldState, UserContext newState);
    }
    ```
    
    - `onStateChange(Long userId, UserContext oldState, UserContext newState)` — вызывается при изменении состояния пользователя. Параметры:
        
        - `userId` — идентификатор пользователя
            
        - `oldState` — предыдущее состояние пользователя (представлено объектом `UserContext`)
            
        - `newState` — новое состояние пользователя (представлено объектом `UserContext`)
            

### Пример использования `UserContext`

```java
UserContext context = new UserContext(UserState.AWAITING_TEXT, List.of("next-step"));
manager.setState(userId, context);
```

### Состояния (`UserState`):
На данный момент библиотека поддерживает только следующие состояния:

- **AWAITING_TEXT** — Ожидание текстового ввода.
    
- **AWAITING_CALLBACK** — Ожидание callback-запроса.
    
- **AWAITING_PHOTO** — Ожидание фотографии.
    
- **AWAITING_MEDIA** — Ожидание других медиафайлов (например, видео, аудио).
  
---
## 🔧 Расширяемость

По умолчанию все внутренние компоненты библиотеки (например, `DefaultStateManager`) можно переопределить. Для этого достаточно определить бин с таким же типом. Библиотека использует `@ConditionalOnMissingBean`, чтобы ваши реализации автоматически подменяли дефолтные.

Например, чтобы переопределить `StateManager`:

```java
@Bean
public StateManager myCustomStateManager() {
    return new MyStateManager();
}
```

## 📎 Дополнительно

- Все методы с аннотациями `@BotCallBack, @BotMedia, @BotPhoto, @BotText` должны принимать `Update` в качестве аргумента.
    
- Поддерживаются Webhook и LongPolling.
    

## 🛡 Безопасность по ролям

Новые пользователи автоматически получают роль `user`. Для изменения роли используется:

```java
manager.setUserRole(userId, "admin");
```
