package adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.cliffdevops.alpha.bismartapp.LoginActivity;
import com.cliffdevops.alpha.bismartapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.AgentItem;
import model.InsuranceItem;

public class InsuranceViewAdapter extends RecyclerView.Adapter<InsuranceViewAdapter.InsuranceViewHolder> {

    private List<InsuranceItem> InsuranceList;
    private OnLoanListener OnLoanListener;

    public InsuranceViewAdapter(List<InsuranceItem> InsuranceList, OnLoanListener onLoanListener) {
        this.InsuranceList = InsuranceList;
        this.OnLoanListener = onLoanListener;
    }

    @NonNull
    @Override
    public InsuranceViewAdapter.InsuranceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.insurance_listview_model, parent, false);
        return new InsuranceViewAdapter.InsuranceViewHolder(itemView, OnLoanListener);
    }

    @Override
    public void onBindViewHolder(@NonNull InsuranceViewAdapter.InsuranceViewHolder holder, int position) {
        final InsuranceItem item = InsuranceList.get(position);
        holder.name.setText(item.getInsuranceName());
        Picasso.get().load(item.getInsuranceIcon()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return InsuranceList.size();
    }

    public interface OnLoanListener {
        void onNoteClick(String position);
    }

    public class InsuranceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name;
        ImageView imageView;
        OnLoanListener mOnLoanListener;
        CardView parentLayout;

        InsuranceViewHolder(View view, OnLoanListener onLoanListener) {
            super(view);
            imageView = view.findViewById(R.id.insuranceIcon);
            name = view.findViewById(R.id.txtInsuranceName);
            parentLayout = view.findViewById(R.id.insuranceView);
            mOnLoanListener = OnLoanListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            final InsuranceItem item = InsuranceList.get(getAdapterPosition());
            mOnLoanListener.onNoteClick(item.getInsuranceName());
        }
    }

}
