package com.pettify.ui.chat;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pettify.R;
import com.pettify.model.chat.Chat;
import com.pettify.model.listeners.EmptyListener;
import com.pettify.model.user.User;
import com.pettify.ui.auth.AuthViewModel;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ChatFragment extends Fragment {

    private ChatViewModel chatNewViewModel;
   ChatNewAdapter adapter;
    RecyclerView chats_list;
    List<Chat> chatData = new LinkedList<>();
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

        chatNewViewModel.getChats().observe(getViewLifecycleOwner(), chats -> {
            chatData = chats;
            adapter.notifyDataSetChanged();
        });
        submit_btn.setOnClickListener(v -> addChat());
        Log.d("TAG",chatData.toString());
        reloadData();


        reloadData();
        return view;
    }

    void reloadData() {
        chatNewViewModel.refreshAllChats(() -> {});
    }


    private void addChat() {

        //submit_btn.setEnabled(false);
        Chat chat=new Chat();
        chat.setMessage(chat_typed_msg.getText().toString());
        chat.setUser_name(user.getName());
        String date=new Date().toString();
        chat.setMsg_date(date);


        EmptyListener listener = () -> {
//            NavController navController = Navigation.findNavController(getView());
//            navController.navigateUp();
            reloadData();
            chat_typed_msg.setText("");
        };
        chatNewViewModel.addChat(chat,listener);
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

        public void bindData(Chat chat) {
            msg.setText(chat.getMessage());
            user_name.setText(chat.getUser_name());
            current_time.setText(chat.getMsg_date());
            Log.d("TAG",chat.getMsg_date().toString());


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
            Chat chat = chatData.get(position);
//            Button delete_report = holder.itemView.findViewById(R.id.listrow_delete_report);
//            Button edit_report = holder.itemView.findViewById(R.id.listrow_edit_report);
            String id = chatNewViewModel.getChats().getValue().get(position).getId();




            holder.bindData(chat);

        }

        @Override
        public int getItemCount() {
            return chatData.size();
        }


    }

}