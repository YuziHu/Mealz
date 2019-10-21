package com.example.mealz.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mealz.Dialogs.AddGroceryDialog;
import com.example.mealz.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GroceryListAdapter extends ArrayAdapter<String> implements AdapterView.OnItemSelectedListener {
    Context context;
    List<String> groceryNames;
    List<Integer> groceryAmount;
    List<String> groceryUnits;
    List<String> groceryShares;

    public GroceryListAdapter(Context c, List<String> names, List<Integer> amount, List<String> units, List<String> shares){
        super(c, R.layout.layout_grocery_item, R.id.groceryName, names);
        this.context = c;
        this.groceryNames = names;
        this.groceryAmount = amount;
        this.groceryUnits = units;
        this.groceryShares = shares;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View grocery_item = layoutInflater.inflate((R.layout.layout_grocery_item),parent, false);
        TextView gName = grocery_item.findViewById(R.id.groceryName);
        TextView gAmount = grocery_item.findViewById(R.id.gAmount);
        TextView gUnit = grocery_item.findViewById(R.id.gUnit);
        TextView gShare = grocery_item.findViewById(R.id.gShare);
        // edit grocery action spinner
        Spinner editGroceryActions = grocery_item.findViewById(R.id.editGrocery);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> editGroceryActionsAdapter = new ArrayAdapter<String>(this.context,
                android.R.layout.simple_list_item_1, this.context.getResources().getStringArray(R.array.editGroceryActions));
        // Specify the layout to use when the list of choices appears
        editGroceryActionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // apply the adapter to the spinner
        editGroceryActions.setAdapter(editGroceryActionsAdapter);
        // set listener on action selected
        editGroceryActions.setOnItemSelectedListener(this);

        gName.setText(groceryNames.get(position));
        // currently no amount field set if user add grocery item through searching a recipe
        if(groceryAmount.get(position)==-1){
            gAmount.setText("");
        }
        else gAmount.setText(groceryAmount.get(position).toString());
        gUnit.setText(groceryUnits.get(position));
        if(groceryShares!=null) {
            System.out.println(groceryShares.get(position));
            gShare.setText(groceryShares.get(position));
        }
        else gShare.setText("");

        return grocery_item;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String actionSelected = adapterView.getItemAtPosition(i).toString();
        if(actionSelected.equals("Edit Quantity")){
            AddGroceryDialog addGroceryDialog = new AddGroceryDialog();
            addGroceryDialog.show(((AppCompatActivity) this.context).getSupportFragmentManager(), "Add grocery item");
        }
//        Toast.makeText(this.context,actionSelected,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
