package dev.karmanov.library.model.methodHolders;

import dev.karmanov.library.model.message.TextType;
import dev.karmanov.library.model.methodHolders.abstractHolders.OrderedActionMethodHolder;

import java.lang.reflect.Method;

public class TextMethodHolder extends OrderedActionMethodHolder {
    private TextType textType;
    private String text;
    private boolean isRegex;
    private int order;

    public TextMethodHolder(Method method, String actionName, TextType textType, String text, boolean isRegex, int order) {
        super(method, actionName,order);
        this.textType = textType;
        this.text = text;
        this.isRegex = isRegex;
    }

    public TextType getTextType() {
        return textType;
    }

    public void setTextType(TextType textType) {
        this.textType = textType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isRegex() {
        return isRegex;
    }

    public void setRegex(boolean regex) {
        isRegex = regex;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
