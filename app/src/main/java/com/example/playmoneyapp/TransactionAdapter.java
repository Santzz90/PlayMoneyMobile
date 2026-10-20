package com.example.playmoneyapp;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.playmoneyapp.data.entity.TransactionEntity;
import java.util.ArrayList;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private List<TransactionEntity> transactions = new ArrayList<>();

    public void setTransactions(List<TransactionEntity> transactions) {
        this.transactions = transactions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        TransactionEntity transaction = transactions.get(position);
        holder.tvDescription.setText(transaction.getDescription());
        holder.tvType.setText(transaction.getType());

        if ("RECEITA".equals(transaction.getType())) {
            holder.tvAmount.setText(String.format("R$ %.2f", transaction.getAmount()));
            holder.tvAmount.setTextColor(Color.parseColor("#2ECC71")); // Verde
        } else {
            holder.tvAmount.setText(String.format("- R$ %.2f", transaction.getAmount()));
            holder.tvAmount.setTextColor(Color.parseColor("#E74C3C")); // Vermelho
        }
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescription, tvType, tvAmount;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvType = itemView.findViewById(R.id.tvType);
            tvAmount = itemView.findViewById(R.id.tvAmount);
        }
    }
}
