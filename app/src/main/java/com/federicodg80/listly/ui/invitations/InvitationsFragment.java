package com.federicodg80.listly.ui.invitations;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.federicodg80.listly.R;
import com.federicodg80.listly.adapters.InvitationAdapter;
import com.federicodg80.listly.databinding.FragmentInvitationsBinding;
import com.federicodg80.listly.models.Invitation;
import com.federicodg80.listly.models.Item;

public class InvitationsFragment extends Fragment {

    private FragmentInvitationsBinding binding;
    private InvitationsViewModel viewModel;
    private InvitationAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(InvitationsViewModel.class);
        binding = FragmentInvitationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Observers
        viewModel.getInvitations().observe(getViewLifecycleOwner(), invitations -> {
            GridLayoutManager glm = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
            binding.recyclerViewInvitations.setLayoutManager(glm);

            adapter = new InvitationAdapter(invitations, getContext(), LayoutInflater.from(getContext()));
            binding.recyclerViewInvitations.setAdapter(adapter);
        });

        viewModel.getIsEmpty().observe(getViewLifecycleOwner(), isEmpty -> {
            if (isEmpty) {
                binding.textNoInvitations.setVisibility(View.VISIBLE);
                binding.recyclerViewInvitations.setVisibility(View.GONE);
            } else {
                binding.textNoInvitations.setVisibility(View.GONE);
                binding.recyclerViewInvitations.setVisibility(View.VISIBLE);
            }
        });

        binding.swipeRefresh.setOnRefreshListener(() -> {
            // Recargar datos
            viewModel.fetchInvitations();

            // Detener animación
            binding.swipeRefresh.postDelayed(() -> {
                binding.swipeRefresh.setRefreshing(false);
            }, 1000);
        });

        viewModel.fetchInvitations();

        attachSwipeHandler(binding.recyclerViewInvitations);

        return root;
    }

    // no se puede mover directamente al ViewModel sin romper la arquitectura MVVM, ya que el método
    // manipula elementos de UI como el RecyclerView, el Adapter y el contexto (getContext()).
    //  El ViewModel debe mantenerse independiente del ciclo de vida y la UI.
    private void attachSwipeHandler(RecyclerView recyclerView) {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();
                if (position == RecyclerView.NO_POSITION) return;

                Invitation item = adapter.invitations.get(position);

                if (direction == ItemTouchHelper.RIGHT) {
                    // Aceptar invitación
                    viewModel.respondToInvitation(item.getInvitationId(), true);
                    adapter.notifyItemChanged(position);
                    // Navegar a la lista aceptada
                    Navigation.findNavController(requireView()).navigate(R.id.navigation_home);
                } else if (direction == ItemTouchHelper.LEFT) {
                    //Rechazar invitación
                    viewModel.respondToInvitation(item.getInvitationId(), false);
                    adapter.invitations.remove(position);
                    adapter.notifyItemRemoved(position);
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    int position = viewHolder.getBindingAdapterPosition();
                    if (position == RecyclerView.NO_POSITION) {
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                        return;
                    }

                    View itemView = viewHolder.itemView;

                    Paint bgPaint = new Paint();
                    Paint textPaint = new Paint();
                    textPaint.setColor(Color.WHITE);
                    textPaint.setTextSize(40f);
                    textPaint.setTextAlign(Paint.Align.LEFT);

                    float centerY = itemView.getTop() + itemView.getHeight() / 2f + 15f;

                    if (dX > 0) {
                        bgPaint.setColor(Color.parseColor("#4CAF50"));
                        c.drawRect(itemView.getLeft(), itemView.getTop(), dX, itemView.getBottom(), bgPaint);

                        textPaint.setTextAlign(Paint.Align.LEFT);
                        c.drawText("aceptar", itemView.getLeft() + 10f, centerY, textPaint);

                    } else if (dX < 0) {
                        bgPaint.setColor(Color.parseColor("#F44336"));
                        c.drawRect(itemView.getRight() + dX, itemView.getTop(), itemView.getRight(), itemView.getBottom(), bgPaint);

                        textPaint.setTextAlign(Paint.Align.RIGHT);
                        c.drawText("no aceptar", itemView.getRight() - 10f, centerY, textPaint);
                    }
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}