package com.example.parentalapp.reward;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.parentalapp.R;

import static com.example.parentalapp.reward.RewardMainActivity.ITEM_NAME;
import static com.example.parentalapp.reward.RewardMainActivity.POINTS;
import static com.example.parentalapp.reward.RewardMainActivity.QUANTITY;

public class RewardConfirmDialog extends AppCompatDialogFragment {
    private RewardConfirmDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View customLayout = layoutInflater.inflate(R.layout.dialog_confirm_reward, null);

        builder.setView(customLayout)
                .setTitle("Confirm Purchase Details")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // empty function
                    }
                })
                .setPositiveButton("Buy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.purchase();
                    }
                });
        initialize(customLayout);

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

    private void initialize(View customLayout){
        TextView textViewquantity = customLayout.findViewById(R.id.textView_confirm_item_quantity);
        textViewquantity.setText("Quantity: " + getArguments().getString(QUANTITY));
        TextView textViewItem = customLayout.findViewById(R.id.textView_confirm_item_name);
        textViewItem.setText(getArguments().getString(ITEM_NAME));
        TextView textViewPoint = customLayout.findViewById(R.id.textView_confirm_reward_points_deduction);
        textViewPoint.setText("Points required: " + getArguments().getString(POINTS));
    }


    public interface RewardConfirmDialogListener{
        void purchase();
    }
}
