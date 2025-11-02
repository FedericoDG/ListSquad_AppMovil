package com.federicodg80.listly.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.federicodg80.listly.R;
import com.federicodg80.listly.models.User;
import com.federicodg80.listly.utils.PreferencesManager;

import java.util.List;

public class CollaboratorAdapter extends RecyclerView.Adapter<CollaboratorAdapter.ViewHolder> {

    public List<User> listaCollaborators;
    private Context context;
    private LayoutInflater inflater;
    private OnRemoveClickListener onRemoveClickListener;
    private User owner;

    // Interfaz para manejar el clic en la X
    public interface OnRemoveClickListener {
        void onRemoveClick(User collaborator, int position);
    }

    public CollaboratorAdapter(List<User> listaCollaborators, Context context, LayoutInflater inflater, OnRemoveClickListener listener) {
        this.listaCollaborators = listaCollaborators;
        this.context = context;
        this.inflater = inflater;
        this.onRemoveClickListener = listener;
    }

    // Método para setear el propietario (owner)
    public void setOwner(User owner) {
        this.owner = owner;
        notifyDataSetChanged(); // Refrescar la vista cuando cambie el propietario
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.collaborator_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User collaborator = listaCollaborators.get(position);
        holder.textViewCollaboratorEmail.setText(collaborator.getEmail());

        // Habilitar el botón X solo si el colaborador es el owner
        String currentUserId = PreferencesManager.getUserUId(context);
        if (owner != null && currentUserId.equals(owner.getUId())) {
            holder.textViewRemoveCollaborator.setEnabled(true);
            holder.textViewRemoveCollaborator.setTextColor(0xFFDD3333);

            holder.textViewRemoveCollaborator.setOnClickListener(v -> {
                int pos = holder.getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && onRemoveClickListener != null) {
                    onRemoveClickListener.onRemoveClick(listaCollaborators.get(pos), pos);
                }
            });
        } else {
            holder.textViewRemoveCollaborator.setEnabled(false);
            holder.textViewRemoveCollaborator.setTextColor(0xFFCCCCCC);
        }
    }

    @Override
    public int getItemCount() {
        return listaCollaborators.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCollaboratorEmail;
        TextView textViewRemoveCollaborator;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCollaboratorEmail = itemView.findViewById(R.id.textViewCollaboratorEmail);
            textViewRemoveCollaborator = itemView.findViewById(R.id.textViewRemoveCollaborator);
        }
    }
}
