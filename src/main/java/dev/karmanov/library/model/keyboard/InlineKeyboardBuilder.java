package dev.karmanov.library.model.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class InlineKeyboardBuilder {
    private final List<List<InlineKeyboardButton>> rows = new ArrayList<>();
    private List<InlineKeyboardButton> currentRow = new ArrayList<>();

    public InlineKeyboardBuilder button(String text, String callBackData){
        InlineKeyboardButton button = new InlineKeyboardButton(text);
        button.setCallbackData(callBackData);
        currentRow.add(button);
        return this;
    }

    public InlineKeyboardBuilder urlButton(String text, String url) {
        InlineKeyboardButton button = new InlineKeyboardButton(text);
        button.setUrl(url);
        currentRow.add(button);
        return this;
    }

    public InlineKeyboardBuilder newRow(){
        if (!currentRow.isEmpty()){
            rows.add(currentRow);
            currentRow = new ArrayList<>();
        }
        return this;
    }

    public InlineKeyboardBuilder row(InlineKeyboardButton... buttons) {
        rows.add(List.of(buttons));
        return this;
    }

    public InlineKeyboardBuilder row(String... textsAndCallbacks) {
        List<InlineKeyboardButton> row = new ArrayList<>();
        for (int i = 0; i + 1 < textsAndCallbacks.length; i += 2) {
            InlineKeyboardButton button = new InlineKeyboardButton(textsAndCallbacks[i]);
            button.setCallbackData(textsAndCallbacks[i + 1]);
            row.add(button);
        }
        rows.add(row);
        return this;
    }

    public boolean isEmpty() {
        return rows.isEmpty() && currentRow.isEmpty();
    }

    public InlineKeyboardMarkup build(){
        newRow();
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardBuilder clear() {
        rows.clear();
        currentRow.clear();
        return this;
    }
}
