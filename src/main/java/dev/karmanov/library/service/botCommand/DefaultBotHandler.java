package dev.karmanov.library.service.botCommand;

import dev.karmanov.library.model.user.DefaultUserContext;
import dev.karmanov.library.model.user.UserState;
import dev.karmanov.library.service.handlers.callback.CallBackHandler;
import dev.karmanov.library.service.handlers.location.LocationHandler;
import dev.karmanov.library.service.handlers.media.MediaHandler;
import dev.karmanov.library.service.handlers.media.document.DocumentHandler;
import dev.karmanov.library.service.handlers.media.photo.PhotoHandler;
import dev.karmanov.library.service.handlers.media.sticker.StickerHandler;
import dev.karmanov.library.service.handlers.media.video.VideoHandler;
import dev.karmanov.library.service.handlers.media.voice.VoiceHandler;
import dev.karmanov.library.service.handlers.schedule.ScheduledHandler;
import dev.karmanov.library.service.handlers.text.TextHandler;
import dev.karmanov.library.service.notify.unexpectedAction.UnexpectedActionNotifier;
import dev.karmanov.library.service.register.utils.media.MediaQualifier;
import dev.karmanov.library.service.state.StateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The BotHandler class is responsible for handling incoming updates Telegram.
 * It manages the state of users, delegates message handling to specific
 * handlers, and ensures that user actions are processed correctly based on their current state.
 */
public class DefaultBotHandler implements BotHandler{
    private StateManager manager;
    private UnexpectedActionNotifier notifier;
    private ExecutorService executorService;
    private MediaQualifier mediaQualifier;
    private MediaHandler mediaHandler;
    private TextHandler textHandler;
    private PhotoHandler photoHandler;
    private StickerHandler stickerHandler;
    private CallBackHandler callBackHandler;
    private DocumentHandler documentHandler;
    private VoiceHandler voiceHandler;
    private LocationHandler locationHandler;
    private ScheduledHandler scheduledHandler;
    private VideoHandler videoHandler;
    private final AtomicBoolean isScheduled = new AtomicBoolean(false);
    private static final Logger logger = LoggerFactory.getLogger(DefaultBotHandler.class);

    @Autowired
    public void setStickerHandler(StickerHandler stickerHandler) {
        this.stickerHandler = stickerHandler;
    }

    @Autowired
    public void setVideoHandler(VideoHandler videoHandler) {
        this.videoHandler = videoHandler;
    }

    @Autowired
    public void setLocationHandler(LocationHandler locationHandler) {
        this.locationHandler = locationHandler;
    }

    /**
     * Sets the UnexpectedActionNotifier used to send notifications to users in case of unexpected actions.
     *
     * @param notifier the UnexpectedActionNotifier instance.
     */
    @Autowired
    public void setNotifier(UnexpectedActionNotifier notifier) {
        this.notifier = notifier;
    }

    /**
     * Sets the MediaHandler used for handling media messages.
     *
     * @param mediaHandler the MediaHandler instance.
     */
    @Autowired
    public void setMediaHandler(MediaHandler mediaHandler) {
        this.mediaHandler = mediaHandler;
    }

    /**
     * Sets the TextHandler used for handling text messages.
     *
     * @param textHandler the TextHandler instance.
     */
    @Autowired
    public void setTextHandler(TextHandler textHandler) {
        this.textHandler = textHandler;
    }

    /**
     * Sets the PhotoHandler used for handling photo messages.
     *
     * @param photoHandler the PhotoHandler instance.
     */
    @Autowired
    public void setPhotoHandler(PhotoHandler photoHandler) {
        this.photoHandler = photoHandler;
    }

    /**
     * Sets the CallBackHandler used for handling callback queries.
     *
     * @param callBackHandler the CallBackHandler instance.
     */
    @Autowired
    public void setCallBackHandler(CallBackHandler callBackHandler) {
        this.callBackHandler = callBackHandler;
    }

    /**
     * Sets the StateManager used to manage the state of users.
     *
     * @param manager the StateManager instance.
     */
    @Autowired
    public void setManager(StateManager manager) {
        this.manager = manager;
    }

    /**
     * Sets the ExecutorService used to execute tasks in separate threads.
     *
     * @param executorService the ExecutorService instance.
     */
    @Autowired
    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    /**
     * Sets the MediaQualifier used to identify media messages.
     *
     * @param qualifier the MediaQualifier instance.
     */
    @Autowired
    public void setMediaQualifier(MediaQualifier qualifier) {
        this.mediaQualifier = qualifier;
    }

    /**
     * Sets the ScheduledHandler used for handling scheduled tasks.
     *
     * @param scheduledHandler the ScheduledHandler instance.
     */
    @Autowired
    public void setScheduledHandler(ScheduledHandler scheduledHandler) {
        this.scheduledHandler = scheduledHandler;
    }

    /**
     * Sets the DocumentHandler used for handling document messages.
     *
     * @param documentHandler the DocumentHandler instance.
     */
    @Autowired
    public void setDocumentHandler(DocumentHandler documentHandler) {
        this.documentHandler = documentHandler;
    }

    /**
     * Sets the VoiceHandler used for handling voice messages.
     *
     * @param voiceHandler the VoiceHandler instance.
     */
    @Autowired
    public void setVoiceHandler(VoiceHandler voiceHandler) {
        this.voiceHandler = voiceHandler;
    }

    /**
     * Handles incoming updates. It initializes user state, checks for
     * the appropriate handler based on the user's current state, and delegates message processing
     * to the relevant handler.
     *
     * @param update the Telegram {@link org.telegram.telegrambots.meta.api.objects.Update} containing the message to be processed.
     */
    @Override
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

            Set<UserState> userStates = manager.getStates(userId);
            Set<String> userAwaitingAction = manager.getUserAction(userId);
            logger.debug("Current user state: {}", userStates);

            if (userStates.contains(UserState.AWAITING_TEXT) && message.hasText()) {
                logger.info("User is awaiting text and received: {}", message.getText());
                executorService.execute(() -> textHandler.handle(userAwaitingAction, update));
            } else if (userStates.contains(UserState.AWAITING_PHOTO) && message.hasPhoto()) {
                logger.info("User is awaiting a photo and it is present.");
                executorService.execute(() -> photoHandler.handle(userAwaitingAction, update));
            } else if (userStates.contains(UserState.AWAITING_DOCUMENT) && message.hasDocument()){
                logger.info("User is awaiting a document and it is present.");
                executorService.execute(()-> documentHandler.handle(userAwaitingAction,update));
            } else if (userStates.contains(UserState.AWAITING_VIDEO) && message.hasVideo()){
                logger.info("User is awaiting a video and it is present.");
                executorService.execute(()-> videoHandler.handle(userAwaitingAction,update));
            } else if (userStates.contains(UserState.AWAITING_STICKER) && message.hasSticker()) {
                logger.info("User is awaiting a sticker and it is present.");
                executorService.execute(()-> stickerHandler.handle(userAwaitingAction,update));
            } else if (userStates.contains(UserState.AWAITING_VOICE) && message.hasVoice()){
                logger.info("User is awaiting a voice and it is present.");
                executorService.execute(()-> voiceHandler.handle(userAwaitingAction,update));
            } else if (userStates.contains(UserState.AWAITING_MEDIA) && mediaQualifier.hasMedia(update) != null) {
                logger.info("User is awaiting media and it is present.");
                executorService.execute(() -> mediaHandler.handle(userAwaitingAction, update));
            } else if (userStates.contains(UserState.AWAITING_LOCATION) && message.hasLocation()){
                logger.info("User is awaiting location and it is present.");
                executorService.execute(()->locationHandler.handle(userAwaitingAction,update));
            } else {
                logger.warn("Unexpected message from user ID: {}. Current state: {}. Message: {}", userId, userStates, message.getText());
                notifier.sendUnexpectedActionMessage(message.getChatId(),userStates);
            }

        } else if (update.hasCallbackQuery()) {
            logger.info("A callback query came through");
            CallbackQuery callback = update.getCallbackQuery();

            Long userId = callback.getFrom().getId();

            Set<UserState> userStates = manager.getStates(userId);
            Set<String> userAwaitingAction = manager.getUserAction(userId);

            logger.debug("User ID: {}. Current state: {}. Awaiting actions: {}", userId, userStates, userAwaitingAction);

            if (userStates.contains(UserState.AWAITING_CALLBACK)) {
                executorService.execute(() -> callBackHandler.handle(userAwaitingAction, update));
            }else {
                logger.warn("Unexpected callback from user ID: {}. Current state: {}", userId, userStates);
                notifier.sendUnexpectedActionMessage(callback.getMessage().getChatId(),userStates);
            }
        }
    }

    /**
     * Initializes the user's state based on the incoming update. If the user has no state,
     * it sets their state to AWAITING_TEXT and assigns the default start action.
     *
     * @param update the incoming update containing a message from the user.
     */
    private void initUserState(Update update) {
        if (update.hasMessage()) {
            Long userId = update.getMessage().getFrom().getId();
            logger.debug("Initializing user state for user ID: {}", userId);

            Set<UserState> currentState = manager.getStates(userId);
            if (currentState == null) {
                logger.info("User ID: {} has no state. Setting to AWAITING_MESSAGE with default action: /start", userId);
                manager.setNextStep(userId, DefaultUserContext.builder()
                                .addState(UserState.AWAITING_TEXT)
                                .addActionData(manager.getDefaultStartActionName())
                        .build());
            } else {
                logger.debug("User ID: {} already has a state: {}", userId, currentState);
            }
        } else {
            logger.info("Update does not contain a message.");
        }
    }
}
