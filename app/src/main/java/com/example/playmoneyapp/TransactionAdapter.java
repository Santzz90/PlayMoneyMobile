package com.example.playmoneyapp;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.playmoneyapp.data.entity.TransactionEntity;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private List<TransactionEntity> transactions = new ArrayList<>();
    private final NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(TransactionEntity transaction);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

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

        String valorFormatado = formatoMoeda.format(transaction.getAmount());

        if ("RECEITA".equals(transaction.getType())) {
            holder.tvAmount.setText(valorFormatado);
            holder.tvAmount.setTextColor(Color.parseColor("#2ECC71")); // Verde
        } else {
            holder.tvAmount.setText("- " + valorFormatado);
            holder.tvAmount.setTextColor(Color.parseColor("#E74C3C")); // Vermelho
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(transaction);
            }
        });
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