package adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cliffdevops.alpha.bismartapp.R;
import com.cliffdevops.alpha.bismartapp.ResultsActivity;
import com.cliffdevops.alpha.bismartapp.SearchActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.AgentItem;

public class AgentViewAdapter extends RecyclerView.Adapter<AgentViewAdapter.AgentViewHolder> {

    private Context context;
    private List<AgentItem> AgentList;
    private OnLoanListener OnLoanListener;
    private String number;

    public AgentViewAdapter(Context context, List<AgentItem> AgentList, OnLoanListener onLoanListener) {
        this.context = context;
        this.AgentList = AgentList;
        this.OnLoanListener = onLoanListener;
    }

    @NonNull
    @Override
    public AgentViewAdapter.AgentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.agent_listview_model, parent, false);
        return new AgentViewHolder(itemView, OnLoanListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AgentViewAdapter.AgentViewHolder holder, int position) {
        final AgentItem item = AgentList.get(position);
        holder.name.setText(item.getName());
        holder.agency.setText(item.getAgency());
        holder.score.setText(item.getScore());
        holder.location.setText(item.getLocation());
        Picasso.get().load(item.getImageURL()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return AgentList.size();
    }

    public interface OnLoanListener {
        void onNoteClick(String position);
    }

    public class AgentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, agency, score, location;
        ImageView imageView;
        OnLoanListener mOnLoanListener;
        CardView parentLayout;

        AgentViewHolder(View view, OnLoanListener onLoanListener) {
            super(view);
            imageView = view.findViewById(R.id.agentProfilePic);
            name = view.findViewById(R.id.txtAgentName);
            agency = view.findViewById(R.id.txtAgency);
            score = view.findViewById(R.id.txtScore);
            location = view.findViewById(R.id.txtLocation);
            parentLayout = view.findViewById(R.id.agentView);
            mOnLoanListener = OnLoanListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            final AgentItem item = AgentList.get(getAdapterPosition());
            number =item.getMobile();
            mOnLoanListener.onNoteClick(item.getMobile());

            ConstraintLayout optionLayout = view.findViewById(R.id.optionView);
            CardView cardView = view.findViewById(R.id.agentView);
            TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
            optionLayout.setVisibility(View.VISIBLE);

            TextView meet = view.findViewById(R.id.btnMeet);
            TextView call = view.findViewById(R.id.btnCall);

            meet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle extras = new Bundle();
                    extras.putCharSequence("name", name.getText());
                    extras.putCharSequence("agency", agency.getText());
                    extras.putCharSequence("location", location.getText());

                    Intent intent = new Intent(context, ResultsActivity.class);
                    intent.putExtras(extras);
                    context.startActivity(intent);
                }
            });
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    makePhoneCall();
                }
            });
        }
    }

    public void makePhoneCall() {
        String dial = "tel:" + number;
        context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
    }

}
