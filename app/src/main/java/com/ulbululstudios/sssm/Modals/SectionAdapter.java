package com.ulbululstudios.sssm.Modals;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.ulbululstudios.sssm.R;

public class SectionAdapter extends FirestoreRecyclerAdapter<Section, SectionAdapter.SectionHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public OnItemClickListener listener;

    public SectionAdapter(@NonNull FirestoreRecyclerOptions<Section> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull SectionHolder holder, int position, @NonNull Section model) {
        holder.tvDepartmentDetail.setText(model.getDepartment());
        holder.tvSectionDetail.setText(model.getSection());
    }

    @NonNull
    @Override
    public SectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_section_grid, parent, false);
        return new SectionHolder(view);
    }

    public void DeleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class SectionHolder extends RecyclerView.ViewHolder {
        protected TextView tvDepartmentDetail; // TODO: implement department text;
        protected TextView tvSectionDetail;

        public SectionHolder(@NonNull View itemView) {
            super(itemView);

            tvDepartmentDetail = itemView.findViewById(R.id.tv_department_detail);
            tvSectionDetail = itemView.findViewById(R.id.tv_section_detail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAbsoluteAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.OnItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface  OnItemClickListener{
        void OnItemClick(DocumentSnapshot documentSnapshot, int position);
    }
}



