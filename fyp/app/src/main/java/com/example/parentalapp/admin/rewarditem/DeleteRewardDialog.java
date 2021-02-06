package com.example.parentalapp.admin.rewarditem;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.example.parentalapp.reward.RewardDBHelper;

public class DeleteRewardDialog extends AppCompatDialogFragment {

    private RewardDBHelper rewardDBHelper;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        rewardDBHelper = new RewardDBHelper(getActivity());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete Reward Item")
                .setMessage("The following Item will be deleted.")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // delete item
                        if(deleteItem(getArguments().getInt(RewardDBHelper.REWARD_ITEM_ID))){
                            Toast.makeText(getActivity(), "Item deleted", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(), "Unable to delete item", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });

        return builder.create();
    }

    private boolean deleteItem(int id){
        return rewardDBHelper.deleteItem(id);
    }
}
