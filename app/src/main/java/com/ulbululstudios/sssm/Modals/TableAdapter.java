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
import com.ulbululstudios.sssm.R;

public class TableAdapter extends FirestoreRecyclerAdapter<TimeTable, TableViewHolder> {
    /***
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public TableAdapter(@NonNull FirestoreRecyclerOptions<TimeTable> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TableViewHolder holder, int position, @NonNull TimeTable model) {
        holder.tvTime.setText(model.getTime());
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
}

class TableViewHolder extends RecyclerView.ViewHolder {

    protected TextView tvTime, tvSubject, tvRoom, tvTeacher;

    public TableViewHolder(@NonNull View itemView) {
        super(itemView);

        tvTime = itemView.findViewById(R.id.tv_time);
        tvSubject = itemView.findViewById(R.id.tv_subject);
        tvRoom = itemView.findViewById(R.id.tv_room);
        tvTeacher = itemView.findViewById(R.id.tv_teacher);
    }
}
