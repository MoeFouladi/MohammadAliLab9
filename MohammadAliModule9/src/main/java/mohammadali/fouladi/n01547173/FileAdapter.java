package mohammadali.fouladi.n01547173;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {

    // creating a variable for array list and context.
    private ArrayList<FileModal> fileArrayList;
    private Foula2i context;

    // creating a constructor for our variables.
    public FileAdapter(ArrayList<FileModal> FileModalArrayList, Foula2i context) {
        this.fileArrayList = FileModalArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public FileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileAdapter.ViewHolder holder, int position) {
        // setting data to our views of recycler view.


            FileModal Fm = fileArrayList.get(position);
            holder.fileName.setText(Fm.getFileName());


    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return fileArrayList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // creating variables for our views.
        private TextView fileName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // initializing our views with their ids.
            fileName = itemView.findViewById(R.id.MoeFileName);

        }
    }
}
