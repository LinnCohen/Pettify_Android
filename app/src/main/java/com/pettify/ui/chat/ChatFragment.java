package com.pettify.ui.chat;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pettify.R;
import com.pettify.model.message.Message;
import com.pettify.model.listeners.EmptyListener;
import com.pettify.model.user.User;
import com.pettify.ui.auth.AuthViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

public class ChatFragment extends Fragment {

    private ChatViewModel chatNewViewModel;
   ChatNewAdapter adapter;
    RecyclerView chats_list;
    List<Message> messageData = new LinkedList<>();
    AuthViewModel authViewModel;
    String currentUserId = "";
    TextView chat_typed_msg;
    Button submit_btn;
    User user;

    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment, container, false);
        chatNewViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        authViewModel =
                new ViewModelProvider(this).get(AuthViewModel.class);
         user = authViewModel.getCurrentUser();
        if(user != null) {
            currentUserId = user.getId();
        }
        chats_list = view.findViewById(R.id.lv_conv);
      //  chats_list.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        chats_list.setLayoutManager(layoutManager);

        adapter = new ChatNewAdapter();
        chats_list.setAdapter(adapter);

        chat_typed_msg=view.findViewById(R.id.input_text2);
        submit_btn = view.findViewById(R.id.send_btn2);

        chatNewViewModel.getMessages().observe(getViewLifecycleOwner(), chats -> {
            messageData = chats;
            adapter.notifyDataSetChanged();
        });
        submit_btn.setOnClickListener(v -> addChat());
        Log.d("TAG", messageData.toString());
        reloadData();


        reloadData();
        return view;
    }

    void reloadData() {
        chatNewViewModel.refreshAllMessages(() -> {});
    }


    private void addChat() {
        if (TextUtils.isEmpty(chat_typed_msg.getText())) {
            return;
        }
        //submit_btn.setEnabled(false);
        Message message =new Message();
        message.setMessage(chat_typed_msg.getText().toString());
//        message.setUser_name(user.getName());
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy, HH:mm");
        String date = format.format(new Date());
        message.setMsg_date(date);

        if (user == null) {
            message.setUser_name("guest");
        } else {
            message.setUser_name(user.getName());
        }

        EmptyListener listener = () -> {
            reloadData();
            chat_typed_msg.setText("");
        };
        chatNewViewModel.addMessage(message,listener);
        reloadData();

    }




    static class ChatNewRowViewHolder extends RecyclerView.ViewHolder {
        TextView msg;
        TextView user_name;
        TextView current_time;


        public ChatNewRowViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            msg = itemView.findViewById(R.id.chatrow_chat_message);
            user_name=itemView.findViewById((R.id.from_user));
            current_time=itemView.findViewById(R.id.current_time);

            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onClick(position);
                    }
                }
            });

        }

        public void bindData(Message message) {
            msg.setText(message.getMessage());
            user_name.setText(message.getUser_name());
            current_time.setText(message.getMsg_date());
        }
    }

    interface OnItemClickListener {
        void onClick(int position);
    }

    class ChatNewAdapter extends RecyclerView.Adapter<ChatNewRowViewHolder> {
        private ChatFragment.OnItemClickListener listener;



        void setOnItemClickListener(ChatFragment.OnItemClickListener listener) {
            this.listener = listener;
        }



        @NonNull
        @Override
        public ChatNewRowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.chat_row, parent, false);
            ChatNewRowViewHolder viewHolder = new ChatNewRowViewHolder(view, listener);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ChatNewRowViewHolder holder, int position) {
            Message message = messageData.get(position);
//            Button delete_report = holder.itemView.findViewById(R.id.listrow_delete_report);
//            Button edit_report = holder.itemView.findViewById(R.id.listrow_edit_report);
            String id = chatNewViewModel.getMessages().getValue().get(position).getId();




            holder.bindData(message);

        }

        @Override
        public int getItemCount() {
            return messageData.size();
        }


    }

}