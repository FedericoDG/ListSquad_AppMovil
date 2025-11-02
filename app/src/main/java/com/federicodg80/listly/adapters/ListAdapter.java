package com.federicodg80.listly.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.federicodg80.listly.R;
import com.federicodg80.listly.models.TaskList;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    public List<TaskList> lists;
    private Context context;
    private LayoutInflater inflater;
    private OnItemClickListener clickListener;

    // --- Interface para el evento de click ---
    public interface OnItemClickListener {
        void onItemClick(TaskList list, int position);
    }

    // --- Constructor ---
    public ListAdapter(List<TaskList> lists, Context context, LayoutInflater inflater) {
        this.lists = lists;
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
        View itemView = inflater.inflate(R.layout.list_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TaskList list = lists.get(position);

        holder.tvTitle.setText(list.getTitle());
        holder.tvDescription.setText(list.getDescription());
        holder.tvIcon.setText(list.getIcon());

        holder.containerGradient.setBackgroundResource(R.drawable.bg_card_tareas);
        //  bg_card_objetivos
        // bg_card_ideas
        // bg_card_default
        // bg_card_compras

        // Evento de click
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onItemClick(list, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvType, tvIcon;
        View containerGradient;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            //tvType = itemView.findViewById(R.id.tvType);
            tvIcon = itemView.findViewById(R.id.tvIcon);
            containerGradient = itemView.findViewById(R.id.containerGradient);
        }
    }
}
