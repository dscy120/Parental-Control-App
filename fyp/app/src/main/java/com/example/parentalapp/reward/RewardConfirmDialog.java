package com.example.parentalapp.reward;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.parentalapp.R;

import org.w3c.dom.Text;

import static com.example.parentalapp.reward.RewardMainActivity.ALLOWANCE;
import static com.example.parentalapp.reward.RewardMainActivity.ITEM_NAME;
import static com.example.parentalapp.reward.RewardMainActivity.POINTS;

public class RewardConfirmDialog extends AppCompatDialogFragment {
    private View customLayout;
    private  RewardConfirmDialogListener listener;
    private View base;
    private TextView textViewquantity, textViewItem, textViewPoint;
    private int itemQuantity, requiredPoints, allowance;
    private String itemName;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        initialize();
        setButtons();

        builder.setView(customLayout);
        builder.setTitle("Confirm Quiz Details")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // empty function
                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.purchase();
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            listener = (RewardConfirmDialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
                    "must implement RewardConfirmDialogListener");
        }
    }

    private void initialize(){
        // set dialog layout
        customLayout = getActivity().getLayoutInflater().inflate(R.layout.dialog_reward_confirm, null);
        // set TextView

        textViewquantity = customLayout.findViewById(R.id.textView_dialog_item_quantity);
        textViewItem = customLayout.findViewById(R.id.textView_dialog_item_name);
        textViewPoint = customLayout.findViewById(R.id.textView_dialog_required_points);

        requiredPoints = getArguments().getInt(POINTS);
        itemName = getArguments().getString(ITEM_NAME);
        itemQuantity = 0;
        allowance = getArguments().getInt(ALLOWANCE);

        updateText();
    }

    private void updateText(){
        textViewItem.setText(itemName);
        textViewPoint.setText("Required points: " + (requiredPoints * itemQuantity) );
        textViewquantity.setText(String.valueOf(itemQuantity));
    }

    private void setButtons(){
        Button button_add = customLayout.findViewById(R.id.button_add_item_quantity);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(requiredPoints * (itemQuantity + 1) < allowance){
                    itemQuantity++;
                }
                updateText();
            }
        });

        Button button_reduce = customLayout.findViewById(R.id.button_reduce_item_quantity);
        button_reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemQuantity > 0){
                    itemQuantity--;
                }
                updateText();
            }
        });
    }

    public interface RewardConfirmDialogListener{
        void purchase();
    }
}
