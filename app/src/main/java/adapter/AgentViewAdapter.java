package adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.cliffdevops.alpha.bismartapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.AgentItem;

public class AgentViewAdapter extends RecyclerView.Adapter<AgentViewAdapter.AgentViewHolder> {

    private List<AgentItem> AgentList;
    private OnAgentListener OnAgentListener;

    public AgentViewAdapter(List<AgentItem> AgentList, OnAgentListener onAgentListener) {
        this.AgentList = AgentList;
        this.OnAgentListener = onAgentListener;
    }

    @NonNull
    @Override
    public AgentViewAdapter.AgentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.agent_listview_model, parent, false);
        return new AgentViewHolder(itemView, OnAgentListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AgentViewAdapter.AgentViewHolder holder, int position) {
        final AgentItem item = AgentList.get(position);
        holder.name.setText(item.getName());
        holder.agency.setText(item.getAgency());
        holder.score.setText(item.getScore());
        holder.location.setText(item.getLocation());
        //Picasso.get().load(item.getImageURL()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return AgentList.size();
    }

    public class AgentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, agency, score, location;
        ImageView imageView;
        OnAgentListener mOnAgentListener;
        CardView parentLayout;

        AgentViewHolder(View view, OnAgentListener onAgentListener) {
            super(view);
            imageView = view.findViewById(R.id.agentProfilePic);
            name = view.findViewById(R.id.txtAgentName);
            agency = view.findViewById(R.id.txtAgency);
            score = view.findViewById(R.id.txtScore);
            location = view.findViewById(R.id.txtLocation);
            parentLayout = view.findViewById(R.id.agentView);
            mOnAgentListener = onAgentListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            final AgentItem item = AgentList.get(getAdapterPosition());
            String Name = item.getName();
            String Agency = item.getAgency();
            String Place = item.getLocation();
            String Mobile = item.getMobile();
            String latitude = item.getLat_coordinate();
            String longitude = item.getLng_coordinate();

            mOnAgentListener.onItemClicked(Name, Agency, Place, Mobile, latitude, longitude);

        }
    }

    public interface OnAgentListener {
        void onItemClicked(String Name, String Agency, String Location, String Mobile, String Latitude,
                           String Longitude);
    }

}