package com.federicodg80.listly.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.federicodg80.listly.R;
import com.federicodg80.listly.models.Item;

import java.util.List;

public class ItemCompraAdapter extends RecyclerView.Adapter<ItemCompraAdapter.ViewHolder> {

    public List<Item> listItems;
    private Context context;
    private LayoutInflater inflater;
    private OnItemClickListener clickListener;

    // --- Interface para el evento de click ---
    public interface OnItemClickListener {
        void onItemClick(Item list, int position);
        void onItemLongClick(Item item, int position);
    }

    // --- Constructor ---
    public ItemCompraAdapter(List<Item> list, Context context, LayoutInflater inflater) {
        this.listItems = list;
        this.context = context;
        this.inflater = inflater;
    }

    // --- Setter para el listener ---
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_compra_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = listItems.get(position);

        holder.textViewCardCompraQuantity.setText(String.valueOf(item.getQuantity()));
        holder.textViewCardCompraUnit.setText(item.getUnit());
        holder.textViewCardCompraTitle.setText(item.getTitle());
        if (item.getDescription() == null || item.getDescription().isEmpty()) {
            holder.textViewCardCompraDescription.setVisibility(View.GONE);
        } else {
            holder.textViewCardCompraDescription.setVisibility(View.VISIBLE);
            holder.textViewCardCompraDescription.setText(item.getDescription());
        }

        // Fondo
        int bgResId;
        if (item.isCompleted()) {
            bgResId = R.drawable.bg_card_compras;
        } else {
            bgResId = R.drawable.bg_card_default;
        }
        holder.containerGradient.setBackgroundResource(bgResId);

        // Icono de mensaje
        if (item.getNotes() == null || item.getNotes().isEmpty()) {
            holder.textViewCardCompraIconMessage.setVisibility(View.GONE);
        } else {
            holder.textViewCardCompraIconMessage.setVisibility(View.VISIBLE);
        }

        // Completado
        if (item.isCompleted()) {
            holder.textViewCardCompraComplete.setText("Comprado");
        } else {
            holder.textViewCardCompraComplete.setText("No Comprado");
        }

        // Evento de click
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onItemClick(item, position);
            }
        });

        // Evento de click sostenido
        holder.itemView.setOnLongClickListener(v -> {
            if (clickListener != null) {
                clickListener.onItemLongClick(item, position);
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return listItems == null ? 0 : listItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCardCompraQuantity,
                textViewCardCompraUnit,
                textViewCardCompraTitle,
                textViewCardCompraDescription,
                textViewCardCompraComplete;
        View containerGradient;
        ImageView textViewCardCompraIconMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCardCompraQuantity = itemView.findViewById(R.id.textViewCardCompraQuantity);
            textViewCardCompraUnit = itemView.findViewById(R.id.textViewCardCompraUnit);
            textViewCardCompraTitle = itemView.findViewById(R.id.textViewCardCompraTitle);
            textViewCardCompraDescription = itemView.findViewById(R.id.textViewCardCompraDescription);
            textViewCardCompraComplete = itemView.findViewById(R.id.textViewCardCompraComplete);
            textViewCardCompraIconMessage = itemView.findViewById(R.id.iconMessage);
            containerGradient = itemView.findViewById(R.id.containerGradient);
        }
    }
}
