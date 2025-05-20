package dev.karmanov.library.config;

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
import dev.karmanov.library.service.register.utils.text.DefaultTextTypeTextQualifier;
import dev.karmanov.library.service.register.utils.text.TextQualifier;
import dev.karmanov.library.service.register.utils.user.DefaultRoleChecker;
import dev.karmanov.library.service.register.utils.user.RoleChecker;
import dev.karmanov.library.service.state.DefaultStateManager;
import dev.karmanov.library.service.state.StateManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
    public Executor methodExecutor(){
        return new DefaultMethodExecutor();
    }

    @Bean
    @ConditionalOnMissingBean(CallBackHandler.class)
    public CallBackHandler callBackHandler(){
        return new DefaultCallBackHandler();
    }

    @Bean
    @ConditionalOnMissingBean(PhotoHandler.class)
    public PhotoHandler photoHandler(DefaultAbsSender sender){
        return new DefaultPhotoHandler(sender);
    }

    @Bean
    @ConditionalOnMissingBean(MediaHandler.class)
    public MediaHandler mediaHandler(){
        return new DefaultMediaMessageHandler();
    }

    @Bean
    @ConditionalOnMissingBean(AccessDeniedNotifier.class)
    public AccessDeniedNotifier accessDeniedNotifier(){
        return new DefaultAccessDeniedNotifier();
    }

    @Bean
    @ConditionalOnMissingBean(ProcessingMessageNotifier.class)
    public ProcessingMessageNotifier processingMessageNotifier(){
        return new DefaultProcessingMessageNotifier();
    }

    @Bean
    @ConditionalOnMissingBean(UnexpectedActionNotifier.class)
    public UnexpectedActionNotifier unexpectedActionNotifier(){
        return new DefaultUnexpectedActionNotifier();
    }

    @Bean
    @ConditionalOnMissingBean(UnexpectedExceptionNotifier.class)
    public UnexpectedExceptionNotifier unexpectedExceptionNotifier(){return new DefaultUnexpectedExceptionMessageNotifier();}

    @Bean
    @ConditionalOnMissingBean(VoiceRegexFailedNotify.class)
    public VoiceRegexFailedNotify voiceRegexFailedNotify(){
        return new DefaultVoiceRegexFailedNotify();
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
    public ExceptionFoundRelevantModelNotifier exceptionFoundRelevantModelNotifier(){
        return new DefaultExceptionFoundRelevantModelNotifier();
    }

    @Bean
    @ConditionalOnMissingBean(LocationHandler.class)
    public LocationHandler locationHandler(){return new DefaultLocationHandler();}

    @Bean
    @ConditionalOnMissingBean(TextHandler.class)
    public TextHandler textHandler(){
        return new DefaultTextHandler();
    }

    @Bean
    @ConditionalOnMissingBean(ScheduledHandler.class)
    public ScheduledHandler scheduledHandler(){
        return new DefaultScheduledMethodHandler();
    }

    @Bean
    @ConditionalOnMissingBean(RoleChecker.class)
    public RoleChecker roleChecker(){return new DefaultRoleChecker();}

    @Bean
    @ConditionalOnMissingBean(DocumentHandler.class)
    public DocumentHandler documentHandler(){
        return new DefaultDocumentHandler();
    }

    @Bean
    @ConditionalOnMissingBean(VoiceHandler.class)
    public VoiceHandler voiceHandler(){return new DefaultVoiceHandler();}

    @Bean
    @ConditionalOnMissingBean(AudioTranscribe.class)
    public AudioTranscribe audioTranscribe(DefaultAbsSender sender) {return new DefaultAudioTranscribe(sender);}
}
