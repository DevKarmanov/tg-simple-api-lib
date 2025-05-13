package dev.karmanov.library.service.register.utils.media.voice;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.karmanov.library.dto.VoiceTextDTO;
import dev.karmanov.library.exception.RelevantVoiceModelAbsentException;
import dev.karmanov.library.service.notify.initModel.InitModelMessageNotifier;
import dev.karmanov.library.service.notify.relevantModelErrorNotifier.ExceptionFoundRelevantModelNotifier;
import jakarta.annotation.PostConstruct;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Voice;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.vosk.Model;
import org.vosk.Recognizer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default implementation of the {@link AudioTranscribe} interface for converting voice messages to text.
 * <p>
 * This class handles the transcription of voice messages using preloaded models and performs audio file
 * processing, such as downloading, converting to WAV format, and recognizing speech.
 * </p>
 */
public class DefaultAudioTranscribe implements AudioTranscribe {
    private static final Logger logger = LoggerFactory.getLogger(DefaultAudioTranscribe.class);
    private InitModelMessageNotifier notifier;
    private ExceptionFoundRelevantModelNotifier exceptionFoundRelevantModelNotifier;
    private final DefaultAbsSender sender;
    private final Map<String, String> languageModels = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, SoftReference<Model>> modelCache = new ConcurrentHashMap<>();
    private String token;

    private boolean initOnStart = false;

    @Autowired(required = false)
    public void setNotifier(InitModelMessageNotifier notifier) {
        this.notifier = notifier;
    }

    @Autowired(required = false)
    public void setExceptionFoundRelevantModelNotifier(ExceptionFoundRelevantModelNotifier exceptionFoundRelevantModelNotifier) {
        this.exceptionFoundRelevantModelNotifier = exceptionFoundRelevantModelNotifier;
    }

    /**
     * Sets bot token for authentication in Telegram.
     *
     * @param token The bot token to be used for authentication.
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Specifies whether to initialize the model at the application startup.
     * <p>
     * By default, models are initialized only upon the first request. If this method is called with {@code true},
     * the models will be initialized at the application startup.
     * </p>
     *
     * @param initOnStart If {@code true}, the models will be initialized at the application startup.
     */
    public void initOnStart(boolean initOnStart) {
        this.initOnStart = initOnStart;
    }

    public DefaultAudioTranscribe(DefaultAbsSender sender){
        this.sender = sender;
        logger.info("DefaultAudioTranscribe initialized with sender: {}", sender);
    }

    @PostConstruct
    public void init() throws IOException {
        if (initOnStart){
            for (String key:languageModels.keySet()){
                try {
                    initModel(key);
                }catch (RelevantVoiceModelAbsentException e){
                    logger.error("Exception occurred: class: {}, reason: {}", e.getClass().getName(), e.getMessage());
                    throw new RuntimeException("class: "+e.getClass()+", reason: "+e.getMessage());
                }

            }
        }
    }

    /**
     * Adds a language model for transcription.
     *
     * @param languageCode The language code (e.g., "en").
     * @param modelPath The path to the language model.
     * @throws IllegalArgumentException If the language code or model path is invalid.
     */
    public void addLanguageModel(String languageCode, String modelPath) {
        if (languageCode == null || languageCode.isEmpty() || modelPath == null || modelPath.isEmpty()) {
            logger.error("Invalid language code or model path provided: languageCode={}, modelPath={}", languageCode, modelPath);
            throw new IllegalArgumentException("Language code and model path must not be null or empty.");
        }
        languageModels.put(languageCode, modelPath);
        logger.info("Added language model for {}: {}", languageCode, modelPath);
    }

    private Model getModel(String language,Long chatId) throws IOException {
        SoftReference<Model> softRef = modelCache.get(language);
        Model model = (softRef != null) ? softRef.get() : null;

        if (model != null) {
            logger.debug("Model for language {} found in cache.", language);
            return model;
        }
        if (!initOnStart){
            return initModelAndNotify(language,chatId);
        }else {
            try {
                return initModel(language);
            }catch (RelevantVoiceModelAbsentException e){
                exceptionFoundRelevantModelNotifier.sendExceptionFoundRelevantModelMessage(chatId,languageModels);
                logger.error("Exception occurred: class: {}, reason: {}", e.getClass().getName(), e.getMessage());
                throw new RuntimeException("class: "+e.getClass()+", reason: "+e.getMessage());
            }
        }
    }

    private Model initModel(String language) throws IOException, RelevantVoiceModelAbsentException {
        logger.debug("Attempting to initialize model for language: {}", language);

        String modelPath = languageModels.get(language);
        if (modelPath == null || modelPath.isEmpty()) {
            logger.error("No model found for language: {}", language);
            throw new RelevantVoiceModelAbsentException("No model found for language: " + language);
        }

        logger.debug("Found model path '{}' for language '{}'", modelPath, language);

        Model model = new Model(modelPath);
        modelCache.put(language, new SoftReference<>(model));

        logger.info("Successfully loaded and cached model for language: {}", language);

        return model;
    }

    private Model initModelAndNotify(String language,Long chatId) throws IOException {
        notifier.sendInitModelMessage(chatId);
        try {
            return initModel(language);
        }catch (RelevantVoiceModelAbsentException e){
            exceptionFoundRelevantModelNotifier.sendExceptionFoundRelevantModelMessage(chatId,languageModels);
            logger.error("Exception occurred: class: {}, reason: {}", e.getClass().getName(), e.getMessage());
            throw new RuntimeException("class: "+e.getClass()+", reason: "+e.getMessage());
        }

    }

    /**
     * Converts a voice message to text using the specified language model.
     *
     * @param voice       The voice message to be transcribed.
     * @param languageCode The language code used for transcription (e.g., "en" for English).
     * @param chatId      The chatID of the user who sent the voice message.
     * @return The transcribed text from the voice message.
     * @throws RuntimeException If an error occurs during transcription or model loading.
     */
    @Override
    public String voiceToText(Voice voice,String languageCode,Long chatId) {
        logger.info("Starting voice transcription process for language: {}", languageCode);
        validateArguments(voice, languageCode);

        Model model;
        try {
            model = getModel(languageCode,chatId);
        } catch (IOException e) {
            logger.error("Error loading model for language: {}", languageCode, e);
            throw new RuntimeException(e);
        }
        logger.debug("Using Model for language: {}", languageCode);

        try {
            File file = getFile(voice);
            logger.debug("Obtained file for voice id {}: {}", voice.getFileId(), file);

            String fileUrl = file.getFileUrl(token);
            logger.debug("Obtained file URL: {}", fileUrl);

            String baseName = "voice_" + System.currentTimeMillis();
            java.io.File downloaded = new java.io.File(baseName + ".oga");
            logger.info("Downloading voice file to: {}", downloaded.getAbsolutePath());
            upload(fileUrl, downloaded);
            logger.info("File downloaded successfully to: {}", downloaded.getAbsolutePath());

            java.io.File wavAudio = convertToWav(downloaded, new java.io.File(baseName + ".wav"));
            logger.info("Converted file to WAV: {}", wavAudio.getAbsolutePath());

            String result = recognizeSpeech(wavAudio, model);
            logger.info("Speech recognized successfully: {}", result);

            deleteAllDownloadedAudio(downloaded, wavAudio);
            logger.debug("Temporary audio files deleted.");

            return result;
        } catch (TelegramApiException | UnsupportedAudioFileException | IOException e) {
            logger.error("Error during voice transcription process", e);
            throw new RuntimeException(e);
        }
    }

    private void validateArguments(Voice voice, String language) {
        if (language == null || language.isEmpty()) {
            String errorMessage = "Language parameter must not be null or empty.";
            logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
        if (voice == null) {
            String errorMessage = "Voice parameter must not be null.";
            logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
        if (token == null || token.isEmpty()) {
            String errorMessage = "Token parameter must not be null or empty.";
            logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
        logger.debug("Arguments validated: language={}, voiceId={}, token={}", language, voice.getFileId(), token);
    }

    private File getFile(Voice voice) throws TelegramApiException {
        logger.debug("Fetching file for voice id: {}", voice.getFileId());
        GetFile getFileMethod = new GetFile();
        getFileMethod.setFileId(voice.getFileId());
        File file = sender.execute(getFileMethod);
        logger.debug("File fetched: {}", file);
        return file;
    }

    private void upload(String fileUrl, java.io.File downloaded) throws IOException {
        logger.debug("Starting file download from URL: {} to local file: {}", fileUrl, downloaded.getAbsolutePath());
        try (InputStream is = new URL(fileUrl).openStream();
             OutputStream os = new FileOutputStream(downloaded)) {
            byte[] buffer = new byte[4096];
            int n;
            while ((n = is.read(buffer)) != -1) {
                os.write(buffer, 0, n);
            }
        }
        logger.debug("File download completed for: {}", downloaded.getAbsolutePath());
    }

    private java.io.File convertToWav(java.io.File input, java.io.File output) {
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(input)) {
            grabber.start();
            try (FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(output, grabber.getAudioChannels())) {
                recorder.setFormat("wav");
                recorder.setAudioCodecName("pcm_s16le");
                recorder.setSampleRate(16000);
                recorder.setAudioChannels(1);
                recorder.start();
                Frame audioFrame;
                while ((audioFrame = grabber.grabSamples()) != null) {
                    recorder.record(audioFrame);
                }
                recorder.stop();
            }
            grabber.stop();
            return output;
        } catch (Exception e) {
            logger.error("ffmpeg conversion failed", e);
            throw new RuntimeException("ffmpeg conversion failed", e);
        }
    }

    private String recognizeSpeech(java.io.File wavAudio, Model model)
            throws IOException, UnsupportedAudioFileException {
        logger.info("Starting speech recognition on WAV file: {}", wavAudio.getAbsolutePath());
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(wavAudio));
             AudioInputStream audioStream = AudioSystem.getAudioInputStream(bis);
             Recognizer recognizer = new Recognizer(model, 16000)) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = audioStream.read(buffer)) >= 0) {
                recognizer.acceptWaveForm(buffer, bytesRead);
            }
            String finalResult = objectMapper
                    .readValue(recognizer.getFinalResult(), VoiceTextDTO.class)
                    .getText();
            logger.info("Speech recognition finished, final result: {}", finalResult);
            return finalResult;
        }
    }

    private void deleteAllDownloadedAudio(java.io.File downloaded, java.io.File wavAudio) {
        logger.debug("Deleting temporary files: {} and {}", downloaded.getAbsolutePath(), wavAudio.getAbsolutePath());
        delAudio(downloaded);
        delAudio(wavAudio);
    }

    private void delAudio(java.io.File audio) {
        for (int i = 0; i < 5; i++) {
            if (audio.delete()) {
                logger.debug("Deleted file: {}", audio.getAbsolutePath());
                return;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
                logger.warn("Interrupted while waiting to delete file: {}", audio.getAbsolutePath());
            }
        }
        logger.error("Failed to delete file: {}", audio.getAbsolutePath());
    }
}

