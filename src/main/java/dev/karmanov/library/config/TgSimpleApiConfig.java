package dev.karmanov.library.config;

import dev.karmanov.library.service.handlers.callback.CallBackHandler;
import dev.karmanov.library.service.handlers.callback.DefaultCallBackHandler;
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
import dev.karmanov.library.service.register.BotCommandRegister;
import dev.karmanov.library.service.register.executor.Executor;
import dev.karmanov.library.service.register.executor.DefaultMethodExecutor;
import dev.karmanov.library.service.register.utils.media.DefaultMediaAvailabilityQualifier;
import dev.karmanov.library.service.register.utils.media.MediaQualifier;
import dev.karmanov.library.service.register.utils.text.TextQualifier;
import dev.karmanov.library.service.register.utils.text.DefaultTextTypeTextQualifier;
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
    public VoiceHandler voiceHandler(){return new DefaultVoiceHandler();
    }
}
