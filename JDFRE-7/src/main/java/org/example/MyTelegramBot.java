package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;

public class MyTelegramBot extends TelegramLongPollingBot {
    public static final String BOT_TOKEN = "Токен бота";
    public static final String BOT_USERNAME = "Имя бота";
    public static final String URI = "https://api.nasa.gov/planetary/apod?api_key= апи ключ наса";
    public static long chat_id;
    public static User userName;

    public MyTelegramBot() throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            botsApi.registerBot(this);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.getMessage().hasText()) {
            chat_id = update.getMessage().getChatId();
            userName = update.getMessage().getFrom();
            switch (update.getMessage().getText()) {
                case "/help":
                    sendMessage("Все твои действия записываются, большой брат бдит!!!");
                    break;
                case "/give":
                    try {
                        sendMessage(Utils.getUrl(URI));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "/start":
                    sendMessage("Привет, я тестовый бот Котёночки - NASA! Я высылаю ссылки на картинки по запросу.\n" +
                            "Напоминаю, что картинки на сайте NASA обновляются раз в сутки\n" +
                            "Команда /give - выводит ежедневную картинку. Команда /help - выдаст секретик");
                    break;
                case "/who":
                    sendMessage(String.valueOf(userName));
                    break;
                default:
                    sendMessage("Не шалите ^^");
            }
        }
    }


    private void sendMessage(String messageText) {
        SendMessage message = new SendMessage();
        message.setChatId(chat_id);
        message.setText(messageText);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}