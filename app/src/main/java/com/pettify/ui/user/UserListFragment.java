package com.pettify.ui.user;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pettify.R;
import com.pettify.model.user.User;
import com.pettify.model.user.UserModel;

import java.util.LinkedList;
import java.util.List;

public class UserListFragment extends Fragment {

    private UserListViewModel userListViewModel;
    ProgressBar pb;
    Button addBtn;
    MyAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);
        userListViewModel = new ViewModelProvider(this).get(UserListViewModel.class);
        ListView list = view.findViewById(R.id.userslist_list);
        pb = view.findViewById(R.id.userslist_progress);
        addBtn = view.findViewById(R.id.userslist_add_btn);
        pb.setVisibility(View.INVISIBLE);

        adapter = new MyAdapter();
        list.setAdapter(adapter);

        addBtn.setOnClickListener(view1 -> addUser());
        userListViewModel.getUserList().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                    adapter.notifyDataSetChanged();
            }
        });
        reloadData();
        return view;
    }

    private void addUser() {
        addBtn.setEnabled(false);
        int id = userListViewModel.getUserList().getValue().size();
        User user = new User();
        user.setId(""+id);
        user.setName("name " + id);
        pb.setVisibility(View.VISIBLE);
        UserModel.instance.addUser(user, () -> reloadData());
    }

    void reloadData(){
        pb.setVisibility(View.VISIBLE);
        addBtn.setEnabled(false);
        UserModel.instance.refreshAllUsers(() -> {
            pb.setVisibility(View.INVISIBLE);
            addBtn.setEnabled(true);
        });
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (userListViewModel.getUserList().getValue() == null) {
                return 0;
            }
            return userListViewModel.getUserList().getValue().size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null){
                view = getLayoutInflater().inflate(R.layout.list_row, null);
            }

            TextView tv = view.findViewById(R.id.listrow_test_tv);
            User user = userListViewModel.getUserList().getValue().get(i);
            tv.setText(user.getId());
            return view;
        }
    }
}