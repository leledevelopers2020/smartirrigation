package com.leledevelopers.smartirrigation.services;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leledevelopers.smartirrigation.R;
import com.leledevelopers.smartirrigation.models.Message;

import java.util.List;

public class MessageAdapters extends RecyclerView.Adapter<MessageAdapters.MessageViewHolder> {
    private List<Message> messageList;

    public MessageAdapters(List<Message> messageList) {
        this.messageList = messageList;
    }


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message, parent, false);
        return new MessageViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.date.setText(message.getDate());
        holder.time.setText(message.getTime());
        holder.action.setText(message.getAction());
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return this.messageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView date, time, action;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            this.date = itemView.findViewById(R.id.messageDate);
            this.time = itemView.findViewById(R.id.messageTime);
            this.action = itemView.findViewById(R.id.messageAction);
        }
    }
}
