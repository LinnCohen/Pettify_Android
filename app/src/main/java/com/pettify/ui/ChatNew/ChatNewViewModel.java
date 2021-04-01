package com.pettify.ui.ChatNew;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.pettify.model.message.Message;
import com.pettify.model.message.MessageModel;
import com.pettify.model.listeners.EmptyListener;


import java.util.List;

public class ChatNewViewModel extends ViewModel {
    private LiveData<List<Message>> messages = MessageModel.instance.getAllMessages();
    private MessageModel messageModel;

    public ChatNewViewModel() {
        messageModel = MessageModel.instance;
    }

    public LiveData<List<Message>> getMessages() {
        return messages;
    }

    public void refreshAllMessages(EmptyListener listener) {
        messageModel.refreshAllMessages(listener);
    }

    public void addMessage(Message message, EmptyListener listener) {
        messageModel.addMessage(message, listener);
    }

}