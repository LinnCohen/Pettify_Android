package com.pettify.ui.chat;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.pettify.model.chat.Chat;
import com.pettify.model.chat.ChatModel;
import com.pettify.model.listeners.EmptyListener;
import com.pettify.model.listeners.Listener;
import com.pettify.model.report.Report;
import com.pettify.model.user.User;


import java.util.List;

public class ChatViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private LiveData<List<Chat>> chats = ChatModel.instance.getAllChats();
    private ChatModel chatModel;

    public ChatViewModel() {
        chatModel=ChatModel.instance;
    }

    public LiveData<List<Chat>> getChats() {
        return chats;
    }

    public void refreshAllChats(EmptyListener listener) {
        chatModel.refreshAllChats(listener);
    }



    public void addChat(Chat chat, EmptyListener listener) {


        chatModel.addChat(chat, listener);
    }


}