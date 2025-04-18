package dev.karmanov.library.model.methodHolders;

import dev.karmanov.library.model.message.TextType;

import java.lang.reflect.Method;

public class TextMethodHolder extends ActionMethodHolder{
    private TextType textType;
    private String text;
    private boolean isRegex;
    private int order;

    public TextMethodHolder(Method method, String actionName, TextType textType, String text, boolean isRegex, int order) {
        super(method, actionName);
        this.textType = textType;
        this.text = text;
        this.isRegex = isRegex;
        this.order = order;
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
