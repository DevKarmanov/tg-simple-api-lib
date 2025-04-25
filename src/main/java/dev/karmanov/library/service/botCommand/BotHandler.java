package dev.karmanov.library.service.botCommand;

import dev.karmanov.library.model.user.UserState;
import dev.karmanov.library.model.user.UserContext;
import dev.karmanov.library.service.handlers.callback.CallBackHandler;
import dev.karmanov.library.service.handlers.media.MediaHandler;
import dev.karmanov.library.service.handlers.media.document.DocumentHandler;
import dev.karmanov.library.service.handlers.media.photo.PhotoHandler;
import dev.karmanov.library.service.handlers.media.voice.VoiceHandler;
import dev.karmanov.library.service.handlers.schedule.ScheduledHandler;
import dev.karmanov.library.service.handlers.text.TextHandler;
import dev.karmanov.library.service.register.utils.media.MediaQualifier;
import dev.karmanov.library.service.state.StateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

public class BotHandler {
    private StateManager manager;
    private ExecutorService executorService;
    private MediaQualifier mediaQualifier;
    private MediaHandler mediaHandler;
    private TextHandler textHandler;
    private PhotoHandler photoHandler;
    private CallBackHandler callBackHandler;
    private DocumentHandler documentHandler;
    private VoiceHandler voiceHandler;
    private ScheduledHandler scheduledHandler;
    private final AtomicBoolean isScheduled = new AtomicBoolean(false);
    private static final Logger logger = LoggerFactory.getLogger(BotHandler.class);

    @Autowired(required = false)
    public void setMediaHandler(MediaHandler mediaHandler) {
        this.mediaHandler = mediaHandler;
    }

    @Autowired(required = false)
    public void setTextHandler(TextHandler textHandler) {
        this.textHandler = textHandler;
    }

    @Autowired(required = false)
    public void setPhotoHandler(PhotoHandler photoHandler) {
        this.photoHandler = photoHandler;
    }

    @Autowired(required = false)
    public void setCallBackHandler(CallBackHandler callBackHandler) {
        this.callBackHandler = callBackHandler;
    }

    @Autowired(required = false)
    public void setManager(StateManager manager){
        this.manager = manager;
    }

    @Autowired(required = false)
    public void setExecutorService(ExecutorService executorService){
        this.executorService = executorService;
    }

    @Autowired(required = false)
    public void setMediaQualifier(MediaQualifier qualifier){
        this.mediaQualifier = qualifier;
    }

    @Autowired(required = false)
    public void setScheduledHandler(ScheduledHandler scheduledHandler) {
        this.scheduledHandler = scheduledHandler;
    }

    @Autowired(required = false)
    public void setDocumentHandler(DocumentHandler documentHandler) {
        this.documentHandler = documentHandler;
    }

    @Autowired(required = false)
    public void setVoiceHandler(VoiceHandler voiceHandler) {
        this.voiceHandler = voiceHandler;
    }

    public void handleMessage(Update update) {
        initUserState(update);

        if (isScheduled.compareAndSet(false, true)) {
            scheduledHandler.startSchedule();
        }

        if (update.hasMessage()) {
            logger.info("A message came through");
            Message message = update.getMessage();
            User from = message.getFrom();
            Long userId = from.getId();

            logger.debug("Received message from user ID: {}", userId);
            logger.debug("Message details - ID: {}, Text: {}, Type: {}", message.getMessageId(), message.getText(), message.getClass().getSimpleName());

            UserState userState = manager.getState(userId);
            List<String> userAwaitingAction = manager.getUserAction(userId);
            logger.debug("Current user state: {}", userState);

            if (userState.equals(UserState.AWAITING_TEXT) && message.hasText()) {
                logger.info("User is awaiting text and received: {}", message.getText());
                executorService.execute(() -> textHandler.handle(userAwaitingAction, update, manager));
            } else if (userState.equals(UserState.AWAITING_PHOTO) && message.hasPhoto()) {
                logger.info("User is awaiting a photo and it is present.");
                executorService.execute(() -> photoHandler.handle(userAwaitingAction, update, manager));
            } else if (userState.equals(UserState.AWAITING_DOCUMENT) && message.hasDocument()){
                logger.info("User is awaiting a document and it is present.");
                executorService.execute(()-> documentHandler.handle(userAwaitingAction,update,manager));
            } else if (userState.equals(UserState.AWAITING_VOICE) && message.hasVoice()){
                logger.info("User is awaiting a voice and it is present.");
                executorService.execute(()-> voiceHandler.handle(userAwaitingAction,update,manager));
            } else if (userState.equals(UserState.AWAITING_MEDIA) && mediaQualifier.hasMedia(update) != null) {
                logger.info("User is awaiting media and it is present.");
                executorService.execute(() -> mediaHandler.handle(userAwaitingAction, update, manager));
            } else {
                logger.warn("Unexpected message from user ID: {}. Current state: {}. Message: {}", userId, userState, message.getText());
            }

        } else if (update.hasCallbackQuery()) {
            logger.info("A callback query came through");
            CallbackQuery callback = update.getCallbackQuery();

            Long userId = callback.getFrom().getId();

            UserState userState = manager.getState(userId);
            List<String> userAwaitingAction = manager.getUserAction(userId);

            logger.debug("User ID: {}. Current state: {}. Awaiting actions: {}", userId, userState, userAwaitingAction);

            if (userState.equals(UserState.AWAITING_CALLBACK)) {
                executorService.execute(() -> callBackHandler.handle(userAwaitingAction, update, manager));
            }
        }
    }


    private void initUserState(Update update) {
        if (update.hasMessage()) {
            Long userId = update.getMessage().getFrom().getId();
            logger.debug("Initializing user state for user ID: {}", userId);

            UserState currentState = manager.getState(userId);
            if (currentState == null) {
                logger.info("User ID: {} has no state. Setting to AWAITING_MESSAGE with default action: /start", userId);
                manager.setState(userId, new UserContext(UserState.AWAITING_TEXT, Collections.singletonList(manager.getDefaultStartActionName())));
            } else {
                logger.debug("User ID: {} already has a state: {}", userId, currentState);
            }
        } else {
            logger.info("Update does not contain a message.");
        }
    }
}
