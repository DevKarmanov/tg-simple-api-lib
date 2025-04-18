package dev.karmanov.library.service.register;

import dev.karmanov.library.annotation.botActivity.RoleBasedAccess;
import dev.karmanov.library.annotation.userActivity.*;
import dev.karmanov.library.model.message.TextType;
import dev.karmanov.library.model.methodHolders.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;

//todo сделать интерфейс и в нем два метода scan() и getBean()
public class BotCommandRegister {
    private final List<MediaMethodHolder> botMediaMethods = new ArrayList<>();
    private final List<TextMethodHolder> botTextMethods = new ArrayList<>();

    private final Map<Method,SpecialAccessMethodHolder> specialAccessMethodHolders = new HashMap<>();

    private final List<PhotoMethodHolder> botPhotoMethods = new ArrayList<>();
    private final List<ScheduledMethodHolder> scheduledMethods = new ArrayList<>();
    private final Map<String, String> beanNames = new HashMap<>();

    private ApplicationContext context;

    private final Map<Class<? extends Annotation>, Consumer<Method>> handlerMap = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(BotCommandRegister.class);

    public BotCommandRegister() {
        handlerMap.put(BotText.class, this::processTextMessage);
        handlerMap.put(BotCallBack.class, this::processCallBack);
        handlerMap.put(BotMedia.class, this::processMediaMessage);
        handlerMap.put(BotPhoto.class, this::processPhotoMessage);
        handlerMap.put(BotScheduled.class, this::processScheduledMethod);
        handlerMap.put(RoleBasedAccess.class, this::processSpecialAccessMethod);
    }

    @Autowired
    private void setContext(ApplicationContext context){
        this.context = context;
    }

    public List<MediaMethodHolder> getBotMediaMethods() {
        return botMediaMethods;
    }

    public List<TextMethodHolder> getBotTextMethods() {
        return botTextMethods;
    }

    public List<PhotoMethodHolder> getBotPhotoMethods() {
        return botPhotoMethods;
    }

    public List<ScheduledMethodHolder> getScheduledMethods() {
        return scheduledMethods;
    }

    public SpecialAccessMethodHolder getSpecialAccessMethodHolders(Method method) {
        return specialAccessMethodHolders.get(method);
    }

    public void scan(){
        String[] beanNamesArray = context.getBeanDefinitionNames();
        logger.debug("Beans found: {}", beanNamesArray.length);

        for (String beanName : beanNamesArray) {
            Object bean = context.getBean(beanName);
            Class<?> beanClass = AopUtils.getTargetClass(bean);

            logger.debug("Processing bean: {} ({})", beanName, beanClass.getSimpleName());


            for (Method method : beanClass.getDeclaredMethods()) {
                if (!containsRelevantAnnotation(method)) continue;
                for (Map.Entry<Class<? extends Annotation>, Consumer<Method>> entry:handlerMap.entrySet()){
                    if (method.isAnnotationPresent(entry.getKey())){
                        beanNames.put(method.toGenericString(), beanName);
                        entry.getValue().accept(method);
                        logger.info("Detected method: {} with annotations: {}", method.getName(), Arrays.toString(method.getDeclaredAnnotations()));
                    }
                }
            }
        }
    }

    private boolean containsRelevantAnnotation(Method method) {
        for (Class<? extends Annotation> annotation : handlerMap.keySet()) {
            if (method.isAnnotationPresent(annotation)) {
                return true;
            }
        }
        return false;
    }


    public Object getBean(Method method){
        String beanName = beanNames.get(method.toGenericString());
        if (beanName == null) {
            logger.error("No bean found for method: {}", method.getName());
            throw new RuntimeException("No bean found for method: " + method.getName());
        }
        return context.getBean(beanName);
    }

    private void processSpecialAccessMethod(Method method){
        RoleBasedAccess roleBasedAccess = method.getAnnotation(RoleBasedAccess.class);
        specialAccessMethodHolders.put(method,new SpecialAccessMethodHolder(
                method,
                new HashSet<>(Arrays.asList(roleBasedAccess.roles()))
        ));
    }

    private void processScheduledMethod(Method method) {
        BotScheduled botScheduled = method.getAnnotation(BotScheduled.class);
        scheduledMethods.add(new ScheduledMethodHolder(
                method,
                botScheduled.order(),
                botScheduled.cron(),
                botScheduled.fixedDelay(),
                botScheduled.fixedRate(),
                botScheduled.zone(),
                botScheduled.runOnStartup(),
                botScheduled.roles()
        ));
    }

    private void processPhotoMessage(Method method){
        BotPhoto botPhoto = method.getAnnotation(BotPhoto.class);
        botPhotoMethods.add(new PhotoMethodHolder(
                method,
                botPhoto.actionName(),
                botPhoto.minFileSize(),
                botPhoto.maxFileSize(),
                botPhoto.order(),
                botPhoto.minWidth(),
                botPhoto.minHeight(),
                botPhoto.aspectRatio(),
                botPhoto.format().toLowerCase(Locale.ROOT).strip()
        ));
    }

    private void processTextMessage(Method method){
        BotText botText = method.getAnnotation(BotText.class);
        botTextMethods.add(new TextMethodHolder(
                method,
                botText.actionName(),
                TextType.TEXT,
                botText.text().toLowerCase(Locale.ROOT).strip(),
                botText.isRegex(),
                botText.order()));
    }

    private void processCallBack(Method method){
        BotCallBack botCallBack = method.getAnnotation(BotCallBack.class);
        Arrays.stream(botCallBack.callbackName())
                .forEach(callBackName->botTextMethods.add(new TextMethodHolder(
                        method,
                        botCallBack.actionName(),
                        TextType.CALLBACK_DATA,
                        callBackName,
                        botCallBack.isRegex(),
                        botCallBack.order()
                        )));
    }
    private void processMediaMessage(Method method){
        BotMedia botMedia = method.getAnnotation(BotMedia.class);
        botMediaMethods.add(new MediaMethodHolder(
                method,
                botMedia.actionName(),
                botMedia.mediaType(),
                botMedia.order()
        ));
    }
}
