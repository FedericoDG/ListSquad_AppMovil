package com.federicodg80.listly.ui.home;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.federicodg80.listly.databinding.FragmentCreateNewListBinding;

import java.util.Objects;

public class CreateNewListFragment extends Fragment {
    private FragmentCreateNewListBinding binding;
    private CreateNewListViewModel viewModel;

    public static CreateNewListFragment newInstance() {
        return new CreateNewListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCreateNewListBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(CreateNewListViewModel.class);

        // Observers
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                binding.textError.setText(error);
                binding.textError.setVisibility(View.VISIBLE);
            } else {
                binding.textError.setVisibility(View.GONE);
            }
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                binding.buttonSubmit.setEnabled(!isLoading);
                binding.buttonSubmit.setText("Creando lista...");
            }
        });

        // Events
        binding.buttonSubmit.setOnClickListener(v -> {
            String title = Objects.requireNonNull(binding.editTextTitle.getText()).toString();
            String description = Objects.requireNonNull(binding.editTextDescription.getText()).toString();
            String type = binding.dropdownType.getText().toString();

            viewModel.createNewList(title, description, type);
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        String[] tipos = new String[] { "Compras"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                tipos
        );

        AutoCompleteTextView dropdownType = binding.dropdownType;
        dropdownType.setAdapter(adapter);
        dropdownType.setOnClickListener(v -> dropdownType.showDropDown());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}