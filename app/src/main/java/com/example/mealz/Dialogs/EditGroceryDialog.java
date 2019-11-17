package com.example.mealz.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mealz.R;

import androidx.appcompat.app.AppCompatDialogFragment;

public class EditGroceryDialog extends AppCompatDialogFragment {
    TextView editGroceryName, editGroceryUnit;
    EditText editGroceryAmount;
    Button setSharedGroceryBtn, updateGroceryBtn, deleteGroceryBtn;
    private EditGroceryDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        return super.onCreateDialog(savedInstanceState);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_grocery, null);

        // get variables from args and initialize variables
        Bundle args = getArguments();
        final String itemID = args.getString("itemID");
        String itemName = args.getString("itemName");
        String itemAmount = args.getString("itemAmount");
        String itemUnit = args.getString("itemUnit");
        //
        editGroceryName = view.findViewById(R.id.editGroceryName);
        editGroceryAmount = view.findViewById(R.id.editGroceryAmount);
        editGroceryUnit = view.findViewById(R.id.editGroceryUnit);
        setSharedGroceryBtn = view.findViewById(R.id.setSharedGroceryBtn);
        updateGroceryBtn = view.findViewById(R.id.updateGroceryBtn);
        deleteGroceryBtn = view.findViewById(R.id.deleteGroceryBtn);
        //
        editGroceryName.setText(itemName);
        editGroceryAmount.setText(itemAmount);
        editGroceryUnit.setText(itemUnit);


        builder.setView(view)
                .setTitle("Edit Grocery")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        setSharedGroceryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setShared(editGroceryName.getText().toString(), Integer.parseInt(editGroceryAmount.getText().toString()), editGroceryUnit.getText().toString(), "");
            }
        });
        updateGroceryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.updateGrocery(editGroceryName.getText().toString(), Integer.parseInt(editGroceryAmount.getText().toString()), editGroceryUnit.getText().toString());
            }
        });
        deleteGroceryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.deleteGrocery(itemID);
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (EditGroceryDialogListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement EditGroceryDialogListener");
        }
    }

    public interface EditGroceryDialogListener{
        void setShared(String groceryName, int amount, String unit, String sharedWith);
        void updateGrocery(String groceryName, int amount, String unit);
        void deleteGrocery(String groceryID);
    }
}
