package dev.karmanov.library.service.register;

import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.stereotype.Component;

@Component
public class BotRegisterInitializer implements SmartInitializingSingleton {

    private final BotCommandRegister register;

    public BotRegisterInitializer(BotCommandRegister register) {
        this.register = register;
    }

    @Override
    public void afterSingletonsInstantiated() {
        register.scan();
    }
}

