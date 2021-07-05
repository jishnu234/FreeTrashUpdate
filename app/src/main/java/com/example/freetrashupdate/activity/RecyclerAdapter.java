package com.example.freetrashupdate.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freetrashupdate.R;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    Context mContext;
    ArrayList<User> dataList;
//    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");


    public RecyclerAdapter(Context context, ArrayList<User> dataList) {
        this.mContext = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_list_layout,
                parent, false);
        ViewHolder holder = new ViewHolder(view);
        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.title.setText("User-" + (position+1) );
        holder.username.setText("Name : "+dataList.get(position).getUsername());
        holder.password.setText("Password : "+dataList.get(position).getPassword());

//        holder.layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new UpdateUser1().deleteUser(position);
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title,username,password;
        LinearLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.dataTitle);
            username = itemView.findViewById(R.id.dataName);
            password = itemView.findViewById(R.id.dataPassword);
//            layout = itemView.findViewById(R.id.list_layout);
        }
    }
}
