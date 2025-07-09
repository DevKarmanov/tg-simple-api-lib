package dev.karmanov.library.service.register;

import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.stereotype.Component;


/**
 * Initializes the {@link BotCommandRegister} after all singleton beans are instantiated.
 * <p>
 * This component is responsible for triggering the {@link BotCommandRegister#scan()} method
 * once all the singleton beans in the Spring context have been initialized.
 * This ensures that the bot command registration process is completed after the entire
 * Spring context is fully set up.
 * </p>
 */
@Component
public class BotRegisterInitializer implements SmartInitializingSingleton {

    private final BotCommandRegister register;
    private final WebHookInitializer webHookInitializer;

    public BotRegisterInitializer(BotCommandRegister register, WebHookInitializer webHookInitializer) {
        this.register = register;
        this.webHookInitializer = webHookInitializer;
    }

    @Override
    public void afterSingletonsInstantiated() {
        register.scan();
        webHookInitializer.registerWebhook();
    }
}

