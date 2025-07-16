package dev.karmanov.library.config;

import dev.karmanov.library.config.botProperties.WebHookBotProperties;
import dev.karmanov.library.service.handlers.callback.CallBackHandler;
import dev.karmanov.library.service.handlers.callback.DefaultCallBackHandler;
import dev.karmanov.library.service.handlers.location.DefaultLocationHandler;
import dev.karmanov.library.service.handlers.location.LocationHandler;
import dev.karmanov.library.service.handlers.media.DefaultMediaMessageHandler;
import dev.karmanov.library.service.handlers.media.MediaHandler;
import dev.karmanov.library.service.handlers.media.document.DefaultDocumentHandler;
import dev.karmanov.library.service.handlers.media.document.DocumentHandler;
import dev.karmanov.library.service.handlers.media.photo.DefaultPhotoHandler;
import dev.karmanov.library.service.handlers.media.photo.PhotoHandler;
import dev.karmanov.library.service.handlers.media.sticker.DefaultStickerHandler;
import dev.karmanov.library.service.handlers.media.sticker.StickerHandler;
import dev.karmanov.library.service.handlers.media.video.DefaultVideoHandler;
import dev.karmanov.library.service.handlers.media.video.VideoHandler;
import dev.karmanov.library.service.handlers.media.voice.DefaultVoiceHandler;
import dev.karmanov.library.service.handlers.media.voice.VoiceHandler;
import dev.karmanov.library.service.handlers.schedule.DefaultScheduledMethodHandler;
import dev.karmanov.library.service.handlers.schedule.ScheduledHandler;
import dev.karmanov.library.service.handlers.text.DefaultTextHandler;
import dev.karmanov.library.service.handlers.text.TextHandler;
import dev.karmanov.library.service.notify.DefaultNotifier;
import dev.karmanov.library.service.notify.Notifier;
import dev.karmanov.library.service.notify.accessDenied.AccessDeniedNotifier;
import dev.karmanov.library.service.notify.accessDenied.DefaultAccessDeniedNotifier;
import dev.karmanov.library.service.notify.initModel.DefaultInitModelMessageNotifier;
import dev.karmanov.library.service.notify.initModel.InitModelMessageNotifier;
import dev.karmanov.library.service.notify.processingMessageNotifier.DefaultProcessingMessageNotifier;
import dev.karmanov.library.service.notify.processingMessageNotifier.ProcessingMessageNotifier;
import dev.karmanov.library.service.notify.relevantModelErrorNotifier.DefaultExceptionFoundRelevantModelNotifier;
import dev.karmanov.library.service.notify.relevantModelErrorNotifier.ExceptionFoundRelevantModelNotifier;
import dev.karmanov.library.service.notify.unexcpectedExceptionMessageNotifier.DefaultUnexpectedExceptionMessageNotifier;
import dev.karmanov.library.service.notify.unexcpectedExceptionMessageNotifier.UnexpectedExceptionNotifier;
import dev.karmanov.library.service.notify.unexpectedAction.DefaultUnexpectedActionNotifier;
import dev.karmanov.library.service.notify.unexpectedAction.UnexpectedActionNotifier;
import dev.karmanov.library.service.notify.voiceRegexFailed.DefaultVoiceRegexFailedNotify;
import dev.karmanov.library.service.notify.voiceRegexFailed.VoiceRegexFailedNotify;
import dev.karmanov.library.service.register.BotCommandRegister;
import dev.karmanov.library.service.register.executor.DefaultMethodExecutor;
import dev.karmanov.library.service.register.executor.Executor;
import dev.karmanov.library.service.register.utils.media.DefaultMediaAvailabilityQualifier;
import dev.karmanov.library.service.register.utils.media.MediaQualifier;
import dev.karmanov.library.service.register.utils.media.voice.AudioTranscribe;
import dev.karmanov.library.service.register.utils.media.voice.DefaultAudioTranscribe;
import dev.karmanov.library.service.register.utils.media.voice.executor.DefaultInterpreterExecutor;
import dev.karmanov.library.service.register.utils.media.voice.executor.InterpreterExecutor;
import dev.karmanov.library.service.register.utils.text.DefaultTextTypeTextQualifier;
import dev.karmanov.library.service.register.utils.text.TextQualifier;
import dev.karmanov.library.service.register.utils.user.DefaultRoleChecker;
import dev.karmanov.library.service.register.utils.user.RoleChecker;
import dev.karmanov.library.service.state.DefaultStateManager;
import dev.karmanov.library.service.state.StateManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.telegram.telegrambots.bots.DefaultAbsSender;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@ComponentScan(basePackages = "dev.karmanov.library")
@EnableConfigurationProperties(WebHookBotProperties.class)
public class TgSimpleApiConfig {

    @Bean
    @ConditionalOnMissingBean(TaskScheduler.class)
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5);
        scheduler.setThreadNamePrefix("tg-scheduler-");
        scheduler.initialize();
        return scheduler;
    }

    @Bean
    @ConditionalOnMissingBean(ExecutorService.class)
    public ExecutorService executorService(){
        return Executors.newCachedThreadPool();
    }

    @Bean
    @ConditionalOnMissingBean(StateManager.class)
    public StateManager stateManager(){
        return new DefaultStateManager();
    }

    @Bean
    @ConditionalOnMissingBean(BotCommandRegister.class)
    public BotCommandRegister botCommandRegister(){
        return new BotCommandRegister();
    }

    @Bean
    @ConditionalOnMissingBean(TextQualifier.class)
    public TextQualifier textTypeQualifier(){
        return new DefaultTextTypeTextQualifier();
    }

    @Bean
    @ConditionalOnMissingBean(MediaQualifier.class)
    public MediaQualifier mediaAvailabilityQualifier(){
        return new DefaultMediaAvailabilityQualifier();
    }

    @Bean
    @ConditionalOnMissingBean(Executor.class)
    public Executor methodExecutor(UnexpectedExceptionNotifier notifier,
                                   BotCommandRegister register){
        return new DefaultMethodExecutor(notifier,register);
    }

    @Bean
    @ConditionalOnMissingBean(CallBackHandler.class)
    public CallBackHandler callBackHandler(Executor methodExecutor,
                                           BotCommandRegister register,
                                           TextQualifier textTypeTextQualifier,
                                           RoleChecker roleChecker){
        return new DefaultCallBackHandler(methodExecutor,register,textTypeTextQualifier,roleChecker);
    }

    @Bean
    @ConditionalOnMissingBean(VideoHandler.class)
    public VideoHandler videoHandler(BotCommandRegister register,
                                     RoleChecker roleChecker,
                                     AudioTranscribe audioTranscribe,
                                     InterpreterExecutor interpreterExecutor
                                     ){
        return new DefaultVideoHandler(register,roleChecker,audioTranscribe,interpreterExecutor);
    }

    @Bean
    @ConditionalOnMissingBean(PhotoHandler.class)
    public PhotoHandler photoHandler(Executor methodExecutor,
                                     BotCommandRegister register,
                                     DefaultAbsSender sender,
                                     RoleChecker roleChecker){
        return new DefaultPhotoHandler(methodExecutor,register,sender,roleChecker);
    }

    @Bean
    @ConditionalOnMissingBean(StickerHandler.class)
    public StickerHandler stickerHandler(Executor methodExecutor,
                                         BotCommandRegister register,
                                         RoleChecker roleChecker){
        return new DefaultStickerHandler(methodExecutor,register,roleChecker);
    }

    @Bean
    @ConditionalOnMissingBean(MediaHandler.class)
    public MediaHandler mediaHandler(MediaQualifier mediaAvailabilityQualifier,
                                     BotCommandRegister register,
                                     Executor methodExecutor,
                                     RoleChecker roleChecker){
        return new DefaultMediaMessageHandler(mediaAvailabilityQualifier,register,methodExecutor,roleChecker);
    }

    @Bean
    @ConditionalOnMissingBean(AccessDeniedNotifier.class)
    public AccessDeniedNotifier accessDeniedNotifier(Notifier notifier){
        return new DefaultAccessDeniedNotifier(notifier);
    }

    @Bean
    @ConditionalOnMissingBean(ProcessingMessageNotifier.class)
    public ProcessingMessageNotifier processingMessageNotifier(Notifier notifier){
        return new DefaultProcessingMessageNotifier(notifier);
    }

    @Bean
    @ConditionalOnMissingBean(UnexpectedActionNotifier.class)
    public UnexpectedActionNotifier unexpectedActionNotifier(Notifier notifier){
        return new DefaultUnexpectedActionNotifier(notifier);
    }

    @Bean
    @ConditionalOnMissingBean(UnexpectedExceptionNotifier.class)
    public UnexpectedExceptionNotifier unexpectedExceptionNotifier(Notifier notifier){
        return new DefaultUnexpectedExceptionMessageNotifier(notifier);
    }

    @Bean
    @ConditionalOnMissingBean(VoiceRegexFailedNotify.class)
    public VoiceRegexFailedNotify voiceRegexFailedNotify(Notifier notifier){
        return new DefaultVoiceRegexFailedNotify(notifier);
    }

    @Bean
    @ConditionalOnMissingBean(Notifier.class)
    public Notifier notifier(DefaultAbsSender sender){
        return new DefaultNotifier(sender);
    }

    @Bean
    @ConditionalOnMissingBean(InitModelMessageNotifier.class)
    public InitModelMessageNotifier initModelMessageNotifier(){
        return new DefaultInitModelMessageNotifier();
    }

    @Bean
    @ConditionalOnMissingBean(ExceptionFoundRelevantModelNotifier.class)
    public ExceptionFoundRelevantModelNotifier exceptionFoundRelevantModelNotifier(Notifier notifier){
        return new DefaultExceptionFoundRelevantModelNotifier(notifier);
    }

    @Bean
    @ConditionalOnMissingBean(LocationHandler.class)
    public LocationHandler locationHandler(Executor methodExecutor,
                                           BotCommandRegister register,
                                           RoleChecker roleChecker){
        return new DefaultLocationHandler(methodExecutor,register,roleChecker);
    }

    @Bean
    @ConditionalOnMissingBean(TextHandler.class)
    public TextHandler textHandler(BotCommandRegister register,
                                   TextQualifier textTypeTextQualifier,
                                   Executor methodExecutor,
                                   RoleChecker roleChecker){
        return new DefaultTextHandler(register,textTypeTextQualifier,methodExecutor,roleChecker);
    }

    @Bean
    @ConditionalOnMissingBean(ScheduledHandler.class)
    public ScheduledHandler scheduledHandler(BotCommandRegister register,
                                             ExecutorService executorService,
                                             Executor methodExecutor,
                                             TaskScheduler scheduler,
                                             StateManager manager){
        return new DefaultScheduledMethodHandler(register,executorService,methodExecutor,scheduler,manager);
    }

    @Bean
    @ConditionalOnMissingBean(RoleChecker.class)
    public RoleChecker roleChecker(AccessDeniedNotifier notifier,
                                   StateManager manager){
        return new DefaultRoleChecker(notifier,manager);
    }

    @Bean
    @ConditionalOnMissingBean(DocumentHandler.class)
    public DocumentHandler documentHandler(Executor methodExecutor,
                                           BotCommandRegister register,
                                           RoleChecker roleChecker){
        return new DefaultDocumentHandler(methodExecutor,register,roleChecker);
    }

    @Bean
    @ConditionalOnMissingBean(InterpreterExecutor.class)
    public InterpreterExecutor interpreterExecutor(Executor methodExecutor,
                                                   ProcessingMessageNotifier notifier,
                                                   ExecutorService executorService,
                                                   VoiceRegexFailedNotify voiceRegexFailedNotify){
        return new DefaultInterpreterExecutor(methodExecutor,notifier,executorService,voiceRegexFailedNotify);
    }

    @Bean
    @ConditionalOnMissingBean(VoiceHandler.class)
    public VoiceHandler voiceHandler(BotCommandRegister register,
                                     RoleChecker roleChecker,
                                     AudioTranscribe audioTranscribe,
                                     InterpreterExecutor interpreterExecutor
    ){
        return new DefaultVoiceHandler(register,roleChecker,audioTranscribe,interpreterExecutor);
    }

    @Bean
    @ConditionalOnMissingBean(AudioTranscribe.class)
    public AudioTranscribe audioTranscribe() {return new DefaultAudioTranscribe();}
}
