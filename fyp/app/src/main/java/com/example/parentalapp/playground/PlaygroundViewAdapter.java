package com.example.parentalapp.playground;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parentalapp.R;

import java.util.ArrayList;

public class PlaygroundViewAdapter extends RecyclerView.Adapter<PlaygroundViewAdapter.PlaygroundViewHolder> {
    private ArrayList<ResolveInfo> appList;
    private final PlaygroundClickListener playgroundClickListener;
    private Context context;

    public PlaygroundViewAdapter(ArrayList<ResolveInfo> appList, PlaygroundClickListener playgroundClickListener, Context context) {
        this.appList = appList;
        this.playgroundClickListener = playgroundClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public PlaygroundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_other_application, parent, false);
        PlaygroundViewHolder evh = new PlaygroundViewHolder(v, this.playgroundClickListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull PlaygroundViewHolder holder, int position) {
        ResolveInfo resolveInfo = appList.get(position);
        PackageManager pm = context.getPackageManager();

        try{
            holder.appIcon.setImageDrawable(pm.getApplicationIcon(resolveInfo.activityInfo.packageName));
            holder.appName.setText(pm.getApplicationLabel(resolveInfo.activityInfo.applicationInfo));
        }catch (PackageManager.NameNotFoundException ne){
            ne.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public static class PlaygroundViewHolder extends RecyclerView.ViewHolder implements View

            .OnClickListener{
        public TextView appName;
        public ImageView appIcon;
        PlaygroundClickListener playgroundClickListener;

        public PlaygroundViewHolder(@NonNull View itemView, PlaygroundClickListener playgroundClickListener) {
            super(itemView);
            appName = itemView.findViewById(R.id.textView_app_name);
            appIcon = itemView.findViewById(R.id.imageView_app_icon);

            this.playgroundClickListener = playgroundClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            playgroundClickListener.onAppClick(getAdapterPosition());
        }
    }



    public interface PlaygroundClickListener{
        void onAppClick(int position);
    }
}
