package com.federicodg80.listly.ui.home;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.federicodg80.listly.MainViewModel;
import com.federicodg80.listly.R;
import com.federicodg80.listly.adapters.CollaboratorAdapter;
import com.federicodg80.listly.adapters.ItemCompraAdapter;
import com.federicodg80.listly.databinding.FragmentListDetailsBinding;
import com.federicodg80.listly.models.Item;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Objects;

public class ListDetailsFragment extends Fragment {
    private FragmentListDetailsBinding binding;
    private ListDetailsViewModel viewModel;
    private ItemCompraAdapter iAdapter;
    private CollaboratorAdapter cAdapter;
    private MainViewModel mainViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(ListDetailsViewModel.class);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        binding = FragmentListDetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        GridLayoutManager glmItems = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        binding.recyclerViewListDetails.setLayoutManager(glmItems);

        GridLayoutManager glmCollaborators = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        binding.recyclerViewCollaborators.setLayoutManager(glmCollaborators);

        iAdapter = new ItemCompraAdapter(new ArrayList<>(), getContext(), inflater);
        cAdapter = new CollaboratorAdapter(
                new ArrayList<>(),
                getContext(),
                inflater,
                (collaborator, position) -> {
                    // Acción al presionar la X
                    cAdapter.listaCollaborators.remove(position);
                    cAdapter.notifyItemRemoved(position);

                    viewModel.removeCollaborator(getArguments(), collaborator.getUId());
                }
        );

        binding.recyclerViewListDetails.setAdapter(iAdapter);
        binding.recyclerViewCollaborators.setAdapter(cAdapter);

        // Listener de click en el adapter (una sola vez)
        iAdapter.setOnItemClickListener(new ItemCompraAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Item item, int position) {
            }

            @Override
            public void onItemLongClick(Item item, int position) {
                // Click sostenido: navegar con todos los datos del item
                Bundle args = new Bundle();
                args.putSerializable("itemId", item.getItemId());

                // Navegar a editar item (cambiá el destino por el que necesites)
                Navigation.findNavController(requireView()).navigate(R.id.navigation_add_item_to_list, args);
            }
        });

        // Observers
       viewModel.getItems().observe(getViewLifecycleOwner(), items -> {
           iAdapter.listItems.clear();
           iAdapter.listItems.addAll(items);
           iAdapter.notifyDataSetChanged();
       });

       viewModel.getList().observe(getViewLifecycleOwner(), taskList -> {
           AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
               appCompatActivity.getSupportActionBar().setTitle(taskList.getTitle());
       });

       viewModel.getOwner().observe(getViewLifecycleOwner(), owner -> {
           binding.ownerDisplayName.setText(owner.getDisplayName());
           binding.ownerEmail.setText(owner.getEmail());
           Glide.with(requireContext())
                   .load(owner.getPhotoUrl())
                   .placeholder(R.drawable.lsq_logo)
                   .error(R.drawable.lsq_logo)
                   .into(binding.ownerAvatar);

           // Pasar el owner al adapter para controlar la visibilidad del botón X
           cAdapter.setOwner(owner);
       });

        viewModel.getCollaborators().observe(getViewLifecycleOwner(), collaborators -> {
            cAdapter.listaCollaborators.clear();
            cAdapter.listaCollaborators.addAll(collaborators);
            cAdapter.notifyDataSetChanged();
        });

        viewModel.getEnableAddCollaboratorBtn().observe(getViewLifecycleOwner(), hasPrivileges -> {
            if (hasPrivileges){
                binding.fabAddCollaborator.setVisibility(View.VISIBLE);
                binding.fabBuyPremium.setVisibility(View.INVISIBLE);
            }else {
                binding.fabAddCollaborator.setVisibility(View.INVISIBLE);
                binding.fabBuyPremium.setVisibility(View.VISIBLE);
            }
        });

        viewModel.getIsEmpty().observe(getViewLifecycleOwner(), isEmpty -> {
            if (isEmpty) {
                binding.recyclerViewListDetails.setVisibility(View.GONE);
                binding.textNoItems.setVisibility(View.VISIBLE);
            } else {
                binding.recyclerViewListDetails.setVisibility(View.VISIBLE);
                binding.textNoItems.setVisibility(View.GONE);
            }
        });

       // Events
        binding.fabAddItem.setOnClickListener(v ->{
            Bundle args = new Bundle();
            args.putInt("listId", getArguments().getInt("listId"));
            Navigation.findNavController(root).navigate(R.id.navigation_add_item_to_list, args );
        });

        binding.fabBuyPremium.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).navigate(R.id.navigation_profile);
        });

        binding.fabAddCollaborator.setOnClickListener(v -> {
            LayoutInflater inflater1 = LayoutInflater.from(getContext());
            View dialogView = inflater1.inflate(R.layout.dialog_invite_collaborator, null);

            TextInputEditText etInviteEmail = dialogView.findViewById(R.id.etInviteEmail);
            MaterialButton btnSendInvite = dialogView.findViewById(R.id.btnSendInvite);

            AlertDialog dialog = new MaterialAlertDialogBuilder(getContext())
                    .setTitle("Invitar colaborador")
                    .setView(dialogView)
                    .create();

            btnSendInvite.setOnClickListener(view -> {
                String email = etInviteEmail.getText().toString().trim();

                viewModel.inviteCollaborator(getArguments(), email);
                dialog.dismiss();
            });

            dialog.show();
        });

        binding.swipeRefresh.setOnRefreshListener(() -> {
            // Recargar datos
            viewModel.loadListDetails(requireArguments());

            // Detener animación
            binding.swipeRefresh.postDelayed(() -> {
                binding.swipeRefresh.setRefreshing(false);
            }, 1000);
        });

        viewModel.loadListDetails(getArguments());

        attachSwipeHandler(binding.recyclerViewListDetails);

        mainViewModel.getRefreshListEvent().observe(getViewLifecycleOwner(), listId -> {
                viewModel.loadListDetails(getArguments());
        });

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

                Item item = iAdapter.listItems.get(position);

                if (direction == ItemTouchHelper.RIGHT) {
                    // Alternar Estado
                    viewModel.toggleItemCompleted(item.getItemId());
                    iAdapter.notifyItemChanged(position);
                } else if (direction == ItemTouchHelper.LEFT) {
                    // Mostrar diálogo de confirmación antes de eliminar
                    new AlertDialog.Builder(requireContext())
                            .setTitle("Confirmar eliminación")
                            .setMessage("¿Estás seguro de que quieres eliminar este item?")
                            .setPositiveButton("Aceptar", (dialog, which) -> {
                                // Ejecutar eliminación
                                viewModel.deleteItem(item.getItemId());
                                iAdapter.listItems.remove(position);
                                iAdapter.notifyItemRemoved(position);
                            })
                            .setNegativeButton("Cancelar", (dialog, which) -> {
                                // Restaurar el item sin cambios
                                iAdapter.notifyItemChanged(position);
                            })
                            .setOnCancelListener(dialog -> {
                                // Si se cancela (ej. back button), restaurar
                                iAdapter.notifyItemChanged(position);
                            })
                            .show();
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
                        c.drawText("cambiar estado", itemView.getLeft() + 10f, centerY, textPaint);

                    } else if (dX < 0) {
                        bgPaint.setColor(Color.parseColor("#F44336"));
                        c.drawRect(itemView.getRight() + dX, itemView.getTop(), itemView.getRight(), itemView.getBottom(), bgPaint);

                        textPaint.setTextAlign(Paint.Align.RIGHT);
                        c.drawText("eliminar", itemView.getRight() - 10f, centerY, textPaint);
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