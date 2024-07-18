package mohammadali.fouladi.n01547173;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



import java.lang.reflect.Type;
import java.util.ArrayList;

// MohammadAli Fouladi N01547173

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Mo1e#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Mo1e extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    // creating variables for our ui components.
    private EditText courseNameEdt, courseDescEdt;
    private Button addBtn, saveBtn, delbtn;
    private RecyclerView courseRV;

    // variable for our adapter class and array list
    private CourseAdapter adapter;
    private ArrayList<CourseModal> courseModalArrayList;


    public Mo1e() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Mo1e.
     */
    // TODO: Rename and change types and number of parameters
    public static Mo1e newInstance(String param1, String param2) {
        Mo1e fragment = new Mo1e();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_mo1e, container, false);
        courseNameEdt = view.findViewById(R.id.MoeCourseedit);
        courseDescEdt = view.findViewById(R.id.MoeDescriptionedit);
        addBtn = view.findViewById(R.id.MoeAdd);
        saveBtn = view.findViewById(R.id.MoeSave);
        delbtn = view.findViewById(R.id.MoeDelete);
        courseRV = view.findViewById(R.id.idRVCourses);
        // calling method to load data
        // from shared prefs.
        loadData();

        // calling method to build
        // recycler view.
        buildRecyclerView();
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // below line is use to add data to array list.
                courseModalArrayList.add(new CourseModal(courseNameEdt.getText().toString(), courseDescEdt.getText().toString()));
                // notifying adapter when new data added.
                adapter.notifyItemInserted(courseModalArrayList.size());
            }
        });
        // on click listener for saving data in shared preferences.
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling method to save data in shared prefs.
                saveData();
                Toast.makeText(getContext(), R.string.saved , Toast.LENGTH_SHORT).show();
            }
        });
        delbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
        return view;
    }
    private void buildRecyclerView() {
        // initializing our adapter class.
        adapter = new CourseAdapter(courseModalArrayList, Mo1e.this);

        // adding layout manager to our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        courseRV.setHasFixedSize(true);

        // setting layout manager to our recycler view.
        courseRV.setLayoutManager(manager);

        // setting adapter to our recycler view.
        courseRV.setAdapter(adapter);
    }
    private void loadData() {
        // method to load arraylist from shared prefs
        // initializing our shared prefs with name as
        // shared preferences.
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(getString(R.string.shared_preferences), MODE_PRIVATE);

        // creating a variable for gson.
        Gson gson = new Gson();

        // below line is to get to string present from our
        // shared prefs if not present setting it as null.
        String json = sharedPreferences.getString("courses", null);

        // below line is to get the type of our array list.
        Type type = new TypeToken<ArrayList<CourseModal>>() {}.getType();

        // in below line we are getting data from gson
        // and saving it to our array list
        courseModalArrayList = gson.fromJson(json, type);

        // checking below if the array list is empty or not
        if (courseModalArrayList == null) {
            // if the array list is empty
            // creating a new array list.
            courseModalArrayList = new ArrayList<>();
        }
    }
    private void delete() {
        // method for deleting the data from array list.
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared_courses", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (courseModalArrayList.size() > 0) {
            courseModalArrayList.remove(courseModalArrayList.size() - 1);
            // Notifying adapter that an item is removed.
            adapter.notifyItemRemoved(courseModalArrayList.size());
            // Save updated list to SharedPreferences.
            saveData();
        }
        else{
        Toast.makeText(getContext(), R.string.no_data_to_delete , Toast.LENGTH_SHORT).show();}

    }

    private void saveData() {
        // method for saving the data in array list.
        // creating a variable for storing data in
        // shared preferences.
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared preferences", MODE_PRIVATE);

        // creating a variable for editor to
        // store data in shared preferences.
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // creating a new variable for gson.
        Gson gson = new Gson();

        // getting data from gson and storing it in a string.
        String json = gson.toJson(courseModalArrayList);

        // below line is to save data in shared
        // prefs in the form of string.
        editor.putString(getString(R.string.courses), json);

        // below line is to apply changes
        // and save data in shared prefs.
        editor.apply();

        // after saving data we are displaying a toast message.

    }
}
