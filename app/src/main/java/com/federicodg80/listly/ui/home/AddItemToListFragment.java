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

import com.federicodg80.listly.R;
import com.federicodg80.listly.databinding.FragmentAddItemToListBinding;

public class AddItemToListFragment extends Fragment {
    private FragmentAddItemToListBinding binding;
    private AddItemToListViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(AddItemToListViewModel.class);
        binding = FragmentAddItemToListBinding.inflate(inflater, container, false);

        // Observers
        viewModel.getSuccessMessage().observe(getViewLifecycleOwner(), message -> {
            binding.textMessage.setVisibility(View.VISIBLE);
            binding.textMessage.setTextColor(getResources().getColor(R.color.green));
            binding.textMessage.setText(message);
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), message -> {
            binding.textMessage.setVisibility(View.VISIBLE);
            binding.textMessage.setTextColor(getResources().getColor(R.color.red));
            binding.textMessage.setText(message);
        });

        viewModel.getCurrentItem().observe(getViewLifecycleOwner(), item -> {
            binding.editTextQuantity.setText(String.valueOf(item.getQuantity()));
            binding.dropdownUnit.setText(item.getUnit());
            binding.dropdownUnit.setEnabled(false); // Deshabilitar edición del dropdown
            binding.editTextTitle.setText(item.getTitle());
            binding.editTextDescription.setText(item.getDescription());
            binding.editTextNotes.setText(item.getNotes());

            binding.buttonAddItem.setVisibility(View.GONE);
            binding.buttonEditItem.setVisibility(View.VISIBLE);
        });

        // Events
        binding.buttonAddItem.setOnClickListener(v -> {
            String quantityStr = binding.editTextQuantity.getText().toString().trim();
            String unit = binding.dropdownUnit.getText().toString().trim();
            String title = binding.editTextTitle.getText().toString().trim();
            String description = binding.editTextDescription.getText().toString().trim();
            String notes = binding.editTextNotes.getText().toString().trim();

            Bundle bundle = getArguments();
            viewModel.addItemToList(bundle, quantityStr, unit, title, description, notes);

            // resetear campos
            binding.editTextQuantity.setText("");
            binding.dropdownUnit.setText("");
            binding.editTextTitle.setText("");
            binding.editTextDescription.setText("");
            binding.editTextNotes.setText("");
        });

        binding.buttonEditItem.setOnClickListener(v -> {
            String quantityStr = binding.editTextQuantity.getText().toString().trim();
            String unit = binding.dropdownUnit.getText().toString().trim();
            String title = binding.editTextTitle.getText().toString().trim();
            String description = binding.editTextDescription.getText().toString().trim();
            String notes = binding.editTextNotes.getText().toString().trim();

            Bundle bundle = getArguments();
            viewModel.updateItem(bundle, quantityStr, unit, title, description, notes);
        });

        viewModel.getItem(getArguments());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String[] tipos = new String[] { "Kg.", "gr.", "Lts.", "cm3", "unidad/es", "docena/s", "lata/s", "paquete/s", "caja/s", "atado/s", "bolsa/s", "sin unidad" };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.dropdown_item_compact,
                tipos
        );

        AutoCompleteTextView dropdownType = binding.dropdownUnit;
        dropdownType.setAdapter(adapter);
        dropdownType.setThreshold(0);
        dropdownType.setOnClickListener(v -> dropdownType.showDropDown());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}