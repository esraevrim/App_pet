package com.example.myapplication;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<ChatMessage> messageList;

    public MessageAdapter(List<ChatMessage> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ChatMessage chatMessage = messageList.get(position);

        if (chatMessage.isUser()) {
            holder.layoutUser.setVisibility(View.VISIBLE);
            holder.layoutBot.setVisibility(View.GONE);
            holder.tvUserMessage.setText(chatMessage.getMessage());
            holder.tvUserTime.setText(chatMessage.getTime());
        } else {
            holder.layoutBot.setVisibility(View.VISIBLE);
            holder.layoutUser.setVisibility(View.GONE);
            holder.tvBotMessage.setText(chatMessage.getMessage());
            holder.tvBotTime.setText(chatMessage.getTime());
        }
    }

    @Override
    public int getItemCount() { return messageList.size(); }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutBot, layoutUser;
        TextView tvBotMessage, tvBotTime, tvUserMessage, tvUserTime;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutBot = itemView.findViewById(R.id.layoutBot);
            layoutUser = itemView.findViewById(R.id.layoutUser);
            tvBotMessage = itemView.findViewById(R.id.tvBotMessage);
            tvBotTime = itemView.findViewById(R.id.tvBotTime);
            tvUserMessage = itemView.findViewById(R.id.tvUserMessage);
            tvUserTime = itemView.findViewById(R.id.tvUserTime);
        }
    }
}
