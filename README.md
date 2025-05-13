
# tg-simple-api

[![License](https://img.shields.io/github/license/DevKarmanov/tg-simple-api-lib)](LICENSE)

`tg-simple-api` — это Java-библиотека с интеграцией Spring, предназначенная для быстрого и удобного создания Telegram-ботов с помощью аннотаций и встроенного менеджера состояний.

## 📦 Подключение

[![](https://jitpack.io/v/DevKarmanov/tg-simple-api-lib.svg)](https://jitpack.io/#DevKarmanov/tg-simple-api-lib)

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
        return new DefaultBotHandler();
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
### `@BotDocument`

Обработка документов:

```java
@BotMedia(actionName = "doc-wait",fileNameRegex = ".*",fileExtensions = "pdf")
public void handleDocument(Update update) { }
```

- **`actionName`** — ID действия.
- **`fileExtensions`** — Ожидаемые расширения.
- **`minFileSize`** — Минимальный размер файла (кб).
- **`maxFileSize`** — Максимальный размер файла (кб).
- **`fileNameRegex`** — Regex имя файла.
- **`order`** — Порядок выполнения.
---
### `@BotVoice`

Обработка голосовых:



### Обработчик голосовых сообщений

```java
@BotMedia(actionName = "voice_wait")
public void handleVoice(Update update) { }
```

Этот метод предназначен для обработки голосовых сообщений. Он использует аннотацию `@BotMedia` и принимает объект `Update` в качестве аргумента.

#### Параметры:
- **`actionName`** — ID действия.
- **`maxDurationSeconds`** — Максимальная длина голосового сообщения.
- **`minDurationSeconds`** — Минимальная длина голосового сообщения.
- **`order`** — Порядок выполнения.
- **`textInterpreter`** — Определяет, нужно ли переводить голосовое сообщение в текст *(по умолчанию: `false`)*.
- **`languageCode`** — Языковая модель для распознавания *(по умолчанию: `en`)*.
- **`regex`** — Фильтр по тексту, полученному из голосового *(по умолчанию: `.*`)*.

#### Особенность:
Если **`textInterpreter = true`**, метод `handleVoice` **должен принимать дополнительный аргумент** `String`, содержащий расшифрованный текст голосового сообщения.

```java
@BotMedia(actionName = "voice_wait")
public void handleVoice(Update update, String transcribedText) { }
```

Таким образом, в зависимости от значения `textInterpreter`, обработчик либо работает с `Update`, либо также получает текстовую интерпретацию голосового сообщения.

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
    
- **`minFileSize`** — Минимальный размер файла (в килобайтах).
    
- **`maxFileSize`** — Максимальный размер файла (в килобайтах).
    
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

- `setNextStep(Long userId, UserContext context)` — задает состояние пользователю. `UserContext` содержит:
    
    - `UserState` — тип ожидаемых взаимодействий (например, текст, фото, callback)
        
    - список допустимых `actionName` — какие действия разрешены в этом состоянии
        
- `addUserRole(Long userId, String role)` — добавляет роль для пользователя (например, "admin")

- `removeUserRole(Long userId, String role)` — забирает роль у пользователя

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
manager.setNextStep(userId, UserContext.builder()
            .addState(Set.of(UserState.AWAITING_VOICE,UserState.AWAITING_DOCUMENT))
            .addActionData(Set.of("voice_wait","doc_wait"))
        .build());
```

### Состояния (`UserState`):
На данный момент библиотека поддерживает только следующие состояния:

- **AWAITING_TEXT** — Ожидание текстового ввода.
- **AWAITING_VOICE** — Ожидание голосового сообщения.
- **AWAITING_DOCUMENT** — Ожидание документа.
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

- Все методы с аннотациями `@BotCallBack, @BotVoice, @BotDocument, @BotMedia, @BotPhoto, @BotText` должны принимать `Update` в качестве аргумента.
    
- Поддерживаются Webhook и LongPolling.
    

## 🛡 Безопасность и уведомления

Новые пользователи автоматически получают роль `user`. Для изменения роли используется:

```java
manager.addUserRole(userId, "admin");
```
---

В библиотеке предусмотрен набор стандартных уведомителей (`Notifier`), которые автоматически отправляют сообщения пользователю в различных ситуациях. Все они реализуют отдельные интерфейсы и по умолчанию используют бин `Notifier`, через который происходит отправка сообщений.

Каждый уведомитель можно переопределить, создав собственный бин соответствующего интерфейса.

---

### 🔐 `DefaultAccessDeniedNotifier`

**Назначение:** уведомляет пользователя об отсутствии прав на выполнение команды.
**Формат сообщения:**

```
❌ You do not have permission to use this command.
Your current roles: <ROLE_1, ROLE_2>
```

**Интерфейс:** `AccessDeniedNotifier`

---

### 🤖 `DefaultInitModelMessageNotifier`

**Назначение:** сообщение об инициализации языковой модели.
**Формат сообщения:**

```
🤖 Initializing the language model... Please wait a moment
```

**Интерфейс:** `InitModelMessageNotifier`

---

### ⏳ `DefaultProcessingMessageNotifier`

**Назначение:** уведомление о том, что запрос обрабатывается (в данный момент используется только во время перевода голоса в текст).
**Формат сообщения:**

```
⏳ Please wait... Your request is being processed.
```

**Интерфейс:** `ProcessingMessageNotifier`

---

### ⚠️ `DefaultExceptionFoundRelevantModelNotifier`

**Назначение:** сообщает о том, что не удалось найти подходящую языковую модель.
**Формат сообщения:**

```
⚠️ An error occurred while trying to find a relevant model. Please try again later.
```

**Интерфейс:** `ExceptionFoundRelevantModelNotifier`

---

### ❗ `DefaultUnexpectedActionNotifier`

**Назначение:** уведомляет пользователя о неожиданном действии (не соответствует текущему состоянию).
**Формат сообщения:**

```
⚠️ I wasn't expecting that action.
Your current state: <STATE_1, STATE_2>
```

**Интерфейс:** `UnexpectedActionNotifier`

---

### 🧪 `DefaultVoiceRegexFailedNotify`

**Назначение:** уведомляет пользователя, если распознанный голосовой текст не соответствует ожидаемому шаблону (`regex`).
**Формат сообщения:**

```
⚠️ Sorry, I couldn't recognize your voice message as a valid command.

🔊 Your input: "<user's voice text>"
🧪 Expected to match: `<regex>`
```

**Интерфейс:** `VoiceRegexFailedNotify`

---

### 🧩 `DefaultNotifier`

**Назначение:** универсальный отправщик сообщений в Telegram, используется всеми `Notifier`-классами.
**Интерфейс:** `Notifier`
**Метод:**

```java
void sendMessage(Long chatId, String text);
```

---

> 💡 Все уведомители используют `@Autowired(required = false)`, поэтому вы можете легко переопределить поведение, создав свой бин нужного интерфейса.

---

## Настройка распознавателя голоса

Вы можете настроить распознаватель голоса в приложении, определив бин `AudioTranscribe`. Пример конфигурации:

```java
@Bean
public AudioTranscribe audioTranscribe() {
    DefaultAudioTranscribe audioTranscribe = new DefaultAudioTranscribe(telegramBot());
    audioTranscribe.setToken(botProperties.token());
    audioTranscribe.initOnStart(true); 
    audioTranscribe.addLanguageModel("ru", "C:/Users/karma/Downloads/vosk-model-ru-0.42");
    return audioTranscribe;
}
```

#### Детали конфигурации:

* **`audioTranscribe.setToken(String token)`**:

    * Устанавливает токен для вашего бота. Токен необходим для общения с API Telegram.

* **`audioTranscribe.setInitOnStart(boolean initOnStart)`**:

    * Если установить в `true`, все модели распознавания голоса инициализируются при старте приложения.
    * Если установить в `false`, модель будет инициализироваться только при первом запросе на распознавание голоса.
    * Поведение по умолчанию: `false` (инициализация только при первом запросе).

* **`audioTranscribe.addLanguageModel(String languageCode, String modelPath)`**:

    * Добавляет языковую модель для конкретного языка.
    * В примере добавляется модель для русского языка из указанного пути.

#### Важные замечания:

* **Не обязательно создавать экземпляр `AudioTranscribe`**: Бин будет автоматически создан, но появится ошибка если попробовать использовать функцию распознавания голоса предварительно не настроив `DefaultAudioTranscribe`.

* **При первом использовании**: Если `initOnStart` установлено в `false`, модель будет инициализирована только при первом запросе на распознавание, и этот процесс может занять некоторое время в зависимости от размера модели.

---

### Используемый ИИ: Vosk

По умолчанию в библиотеке используется ИИ для распознавания голоса **Vosk**. Vosk — это система распознавания речи с открытым исходным кодом, которая поддерживает множество языков и работает на различных платформах.

#### Скачивание языковых моделей для Vosk

Для корректной работы распознавания голоса необходимо скачать и добавить соответствующую языковую модель. Вы можете найти различные модели для Vosk на официальной странице:

* [Скачивание языковых моделей для Vosk](https://alphacephei.com/vosk/models)

Просто выберите модель для нужного языка и укажите путь к ней в конфигурации, как показано ниже:

```java
audioTranscribe.addLanguageModel("ru", "C:/path/to/vosk-model-ru");
```

Пример для русского языка:

```java
audioTranscribe.addLanguageModel("ru", "C:/Users/karma/Downloads/vosk-model-ru-0.42");
```

#### Поддерживаемые языки

Vosk поддерживает множество языков, включая, но не ограничиваясь:

* Русский
* Английский
* Испанский
* Французский
* Немецкий

Для других языков можно найти модели на указанном выше сайте или в репозиториях с открытым исходным кодом.

