// java
package com.federicodg80.listly.ui.home;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import java.util.ArrayList;

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

            // Listener de click en el adapter (una sola vez)
            adapter.setOnItemClickListener((list, position) -> {
                // Navegar a detalles de la lista
                Bundle args = new Bundle();
                args.putInt("listId", list.getListId());
                Navigation.findNavController(root).navigate(R.id.navigation_list_details, args );
            });
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null && isLoading) {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.fabAddList.setVisibility(View.GONE);
                binding.contentLayout.setVisibility(View.GONE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
                binding.contentLayout.setVisibility(View.VISIBLE);
                binding.fabAddList.setVisibility(View.VISIBLE);
            }
        });

        // FAB
        binding.fabAddList.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_createNewListFragment)
        );

        // ya se que no le gustan los if en el fragement, pero necesito evitar recargas dobles
        if (viewModel.getLists().getValue() == null) {
            viewModel.loadLists();
        }

        return root;
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
