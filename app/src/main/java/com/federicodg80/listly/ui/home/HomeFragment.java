package com.federicodg80.listly.ui.home;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.federicodg80.listly.R;
import com.federicodg80.listly.adapters.ListAdapter;
import com.federicodg80.listly.databinding.FragmentHomeBinding;
import com.federicodg80.listly.models.TaskList;
import com.federicodg80.listly.utils.PreferencesManager;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private ListAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Observers
        viewModel.getLists().observe(getViewLifecycleOwner(), lists -> {
            GridLayoutManager glm = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
            binding.recyclerViewLists.setLayoutManager(glm);

            adapter = new ListAdapter(lists, getContext(), LayoutInflater.from(getContext()));
            binding.recyclerViewLists.setAdapter(adapter);

            // Activar el swipe handler
            attachSwipeHandler(binding.recyclerViewLists);

            // Listener de click en el adapter (una sola vez)
            adapter.setOnItemClickListener((list, position) -> {
                // Navegar a detalles de la lista
                Bundle args = new Bundle();
                args.putInt("listId", list.getListId());
                Navigation.findNavController(root).navigate(R.id.navigation_list_details, args );
            });
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.contentLayout.setVisibility(View.GONE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
                binding.contentLayout.setVisibility(View.VISIBLE);
                binding.fabAddList.setVisibility(View.VISIBLE);
            }
        });


        viewModel.getIsEmpty().observe(getViewLifecycleOwner(), isEmpty -> {
            if (isEmpty) {
                binding.textNoLists.setVisibility(View.VISIBLE);
                binding.recyclerViewLists.setVisibility(View.GONE);
            } else {
                binding.textNoLists.setVisibility(View.GONE);
                binding.recyclerViewLists.setVisibility(View.VISIBLE);
            }
        });

        // FAB
        binding.fabAddList.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_createNewListFragment)
        );

        binding.swipeRefresh.setOnRefreshListener(() -> {
            // Recargar datos
            viewModel.loadLists();

            // Detener animación
            binding.swipeRefresh.postDelayed(() -> {
                binding.swipeRefresh.setRefreshing(false);
            }, 1000);
        });

        // ya se que no le gustan los if en el fragement, pero necesito evitar recargas dobles
        if (viewModel.getLists().getValue() == null) {
            viewModel.loadLists();
        }

        return root;
    }

    // no se puede mover directamente al ViewModel sin romper la arquitectura MVVM, ya que el método
    // manipula elementos de UI como el RecyclerView, el Adapter y el contexto (getContext()).
    //  El ViewModel debe mantenerse independiente del ciclo de vida y la UI.
    private void attachSwipeHandler(RecyclerView recyclerView) {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getBindingAdapterPosition();
                if (position == RecyclerView.NO_POSITION || adapter == null || adapter.lists == null) {
                    return 0; // Sin movimientos permitidos
                }

                // Verificar si el usuario es propietario de la lista
                TaskList taskList = adapter.lists.get(position);
                String currentUserUid = PreferencesManager.getUserUId(requireContext());

                // Solo permitir swipe si el usuario es el propietario
                if (currentUserUid != null && currentUserUid.equals(taskList.getOwnerUid())) {
                    return makeMovementFlags(0, ItemTouchHelper.LEFT);
                } else {
                    return 0; // Sin movimientos permitidos si no es propietario
                }
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();
                if (position == RecyclerView.NO_POSITION) return;

                TaskList taskList = adapter.lists.get(position);

                // Solo swipe hacia la izquierda - eliminar
                // Mostrar diálogo de confirmación antes de eliminar
                new AlertDialog.Builder(requireContext())
                        .setTitle("Confirmar eliminación")
                        .setMessage("¿Estás seguro de que quieres eliminar esta lista?")
                        .setPositiveButton("Aceptar", (dialog, which) -> {
                            // Ejecutar eliminación usando el ViewModel
                            viewModel.deleteList(taskList.getListId());
                        })
                        .setNegativeButton("Cancelar", (dialog, which) -> {
                            // Restaurar la lista sin cambios
                            adapter.notifyItemChanged(position);
                        })
                        .setOnCancelListener(dialog -> {
                            // Si se cancela (ej. back button), restaurar
                            adapter.notifyItemChanged(position);
                        })
                        .show();
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

                    // Solo swipe hacia la izquierda - eliminar
                    if (dX < 0) {
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
    public void onResume() {
        super.onResume();
        viewModel.loadLists();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
