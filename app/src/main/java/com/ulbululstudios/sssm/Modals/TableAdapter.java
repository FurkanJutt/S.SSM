package com.ulbululstudios.sssm.Modals;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.ulbululstudios.sssm.R;

public class TableAdapter extends FirestoreRecyclerAdapter<TimeTable, TableAdapter.TableViewHolder> {
    public OnItemClickListener listener;

    public TableAdapter(@NonNull FirestoreRecyclerOptions<TimeTable> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TableViewHolder holder, int position, @NonNull TimeTable model) {
        holder.tvTime.setText(model.getTimeFrom()+ " - " + model.getTimeTo());
        holder.tvSubject.setText(model.getSubject());
        holder.tvRoom.setText(model.getRoom());
        holder.tvTeacher.setText(model.getTeacher());
    }

    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_section_table, parent, false);
        return new TableViewHolder(view);
    }

    public void DeleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class TableViewHolder extends RecyclerView.ViewHolder {

        protected TextView tvTime, tvSubject, tvRoom, tvTeacher;

        public TableViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTime = itemView.findViewById(R.id.tv_time);
            tvSubject = itemView.findViewById(R.id.tv_subject);
            tvRoom = itemView.findViewById(R.id.tv_room);
            tvTeacher = itemView.findViewById(R.id.tv_teacher);

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

    public interface OnItemClickListener{
        void OnItemClick(DocumentSnapshot documentSnapshot, int position);
    }
}


