package com.federicodg80.listly.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.federicodg80.listly.R;
import com.federicodg80.listly.models.Invitation;
import com.federicodg80.listly.models.TaskList;

import java.util.List;

public class InvitationAdapter extends RecyclerView.Adapter<InvitationAdapter.ViewHolder> {

    public List<Invitation> invitations;
    private Context context;
    private LayoutInflater inflater;
    private ListAdapter.OnItemClickListener clickListener;

    public InvitationAdapter(List<Invitation> invitations, Context context, LayoutInflater inflater) {
        this.invitations = invitations;
        this.context = context;
        this.inflater = inflater;
    }


    @NonNull
    @Override
    public InvitationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_invitation, parent, false);
        return new InvitationAdapter.ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull InvitationAdapter.ViewHolder holder, int position) {
        Invitation invitation = invitations.get(position);

        holder.tvListTitle.setText(invitation.getList().getTitle());
        holder.tvListDescription.setText(invitation.getList().getDescription());
        holder.tvFromUserName.setText(invitation.getFromUser().getDisplayName());
        holder.tvFromUserEmail.setText(invitation.getFromUser().getEmail());

        Glide.with(context)
                .load(invitation.getFromUser().getPhotoUrl())
                .placeholder(R.drawable.lsq_logo)
                .error(R.drawable.lsq_logo)
                .into(holder.imgFromUserPhoto);
    }

    @Override
    public int getItemCount() {
        return invitations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvListTitle, tvListDescription, tvFromUserName, tvFromUserEmail;
        ImageView imgFromUserPhoto;
        // View containerGradient;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvListTitle = itemView.findViewById(R.id.tvListTitle);
            tvListDescription = itemView.findViewById(R.id.tvListDescription);
            tvFromUserName = itemView.findViewById(R.id.tvFromUserName);
            tvFromUserEmail = itemView.findViewById(R.id.tvFromUserEmail);
            imgFromUserPhoto = itemView.findViewById(R.id.imgFromUserPhoto);
            // containerGradient = itemView.findViewById(R.id.containerGradient);
        }
    }
}
