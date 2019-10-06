package com.example.mealz.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.mealz.Activities.HomeActivity;
import com.example.mealz.R;

import androidx.appcompat.app.AppCompatDialogFragment;

public class AddGroceryDialog extends AppCompatDialogFragment {
    private EditText addGroceryName, addGroceryAmount;
    private Spinner groceryUnitSpinner;

    @Override
    public Dialog onCreateDialog(Bundle savedInstance){
//        return super.onCreateDialog(savedInstance);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_grocery_dialog, null);

        builder.setView(view)
                .setTitle("Add an item to your grocery list")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        addGroceryName = view.findViewById(R.id.addGroceryName);
        addGroceryAmount = view.findViewById(R.id.addGroceryAmount);
        // making the unit selection spinner
        groceryUnitSpinner = view.findViewById(R.id.addGroceryUnit);
        ArrayAdapter<String> unitSelectionAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.groceryUnits));
        unitSelectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groceryUnitSpinner.setAdapter(unitSelectionAdapter);
        return builder.create();
    }
}
