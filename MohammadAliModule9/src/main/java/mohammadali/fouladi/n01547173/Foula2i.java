package mohammadali.fouladi.n01547173;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Foula2i#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Foula2i extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final int MAX_FILES = 3;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ToggleButton fileType;
    private EditText fileName, fileContents;
    private LinearLayout fileNamesLayout;
    private RecyclerView fileRV;
    private FileAdapter adapter;
    private ArrayList<FileModal> fileArrayList;;

    public Foula2i() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Foula2i.
     */
    // TODO: Rename and change types and number of parameters
    public static Foula2i newInstance(String param1, String param2) {
        Foula2i fragment = new Foula2i();
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
        View view =inflater.inflate(R.layout.fragment_foula2i, container, false);
        fileName = view.findViewById(R.id.activity_internalstorage_filename);
        fileContents = view.findViewById(R.id.activity_internalstorage_filecontents);

        fileType = view.findViewById(R.id.activity_internalstorage_filetype);
        fileType.setChecked(true);
        fileRV = view.findViewById(R.id.RVFiles);
        loadData();
        buildRecyclerView();
        view.findViewById(R.id.activity_internalstorage_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFile(getContext(), fileType.isChecked());


            }
        });

        view.findViewById(R.id.activity_internalstorage_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFile(getContext(), fileType.isChecked());
            }
        });

        view.findViewById(R.id.activity_internalstorage_write).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeFile(getContext(), fileType.isChecked());
            }
        });

        view.findViewById(R.id.activity_internalstorage_read).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readFile(getContext(), fileType.isChecked());
            }
        });
        return view;
    }
    private void createFile(Context context, boolean isPersistent) {
        File file;
        if (isPersistent) {
            file = new File(context.getFilesDir(), fileName.getText().toString());
        } else {
            file = new File(context.getCacheDir(), fileName.getText().toString());
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
                Toast.makeText(context, String.format("File %s has been created", fileName.getText().toString()), Toast.LENGTH_SHORT).show();
                fileArrayList.add(new FileModal(fileName.getText().toString()));
                // notifying adapter when new data added.
                adapter.notifyItemInserted(fileArrayList.size());
            } catch (IOException e) {
                Toast.makeText(context, String.format("File %s creation failed", fileName.getText().toString()), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, String.format("File %s already exists", fileName.getText().toString()), Toast.LENGTH_SHORT).show();
        }
    }

    private void writeFile(Context context, boolean isPersistent) {
        try {
            FileOutputStream fileOutputStream;
            if (isPersistent) {
                fileOutputStream = context.openFileOutput(fileName.getText().toString(), Context.MODE_PRIVATE);
            } else {
                File file = new File(context.getCacheDir(), fileName.getText().toString());
                fileOutputStream = new FileOutputStream(file);
            }
            fileOutputStream.write(fileContents.getText().toString().getBytes(Charset.forName("UTF-8")));
            Toast.makeText(context, String.format("Write to %s successful", fileName.getText().toString()), Toast.LENGTH_SHORT).show();
            fileArrayList.add(new FileModal(fileName.getText().toString()));
            // notifying adapter when new data added.
            adapter.notifyItemInserted(fileArrayList.size());
            saveData();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, String.format("Write to file %s failed", fileName.getText().toString()), Toast.LENGTH_SHORT).show();
        }
    }

    private void readFile(Context context, boolean isPersistent) {
        try {
            FileInputStream fileInputStream;
            if (isPersistent) {
                fileInputStream = context.openFileInput(fileName.getText().toString());
            } else {
                File file = new File(context.getCacheDir(), fileName.getText().toString());
                fileInputStream = new FileInputStream(file);
            }

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, Charset.forName("UTF-8"));
            List<String> lines = new ArrayList<String>();
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            fileContents.setText(TextUtils.join("\n", lines));
            Toast.makeText(context, String.format("Read from file %s successful", fileName.getText().toString()), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, String.format("Read from file %s failed", fileName.getText().toString()), Toast.LENGTH_SHORT).show();
            fileContents.setText("");

        }
    }

    private void deleteFile(Context context, boolean isPersistent) {
        File file;
        if (isPersistent) {
            file = new File(context.getFilesDir(), fileName.getText().toString());
        } else {
            file = new File(context.getCacheDir(), fileName.getText().toString());
        }
        if (file.exists()) {
            file.delete();
            delete();
            Toast.makeText(context, String.format("File %s has been deleted", fileName.getText().toString()), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, String.format("File %s doesn't exist", fileName.getText().toString()), Toast.LENGTH_SHORT).show();
        }
    }
    private void buildRecyclerView() {
        // initializing our adapter class.
        adapter = new FileAdapter(fileArrayList, Foula2i.this);

        // adding layout manager to our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        fileRV.setHasFixedSize(true);

        // setting layout manager to our recycler view.
        fileRV.setLayoutManager(manager);

        // setting adapter to our recycler view.
        fileRV.setAdapter(adapter);
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
        String json = sharedPreferences.getString("files", null);

        // below line is to get the type of our array list.
        Type type = new TypeToken<ArrayList<FileModal>>() {}.getType();

        // in below line we are getting data from gson
        // and saving it to our array list
        fileArrayList = gson.fromJson(json, type);

        // checking below if the array list is empty or not
        if (fileArrayList == null) {
            // if the array list is empty
            // creating a new array list.
            fileArrayList = new ArrayList<>();
        }
    }
    private void delete() {
        // method for deleting the data from array list.
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared_files", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (fileArrayList.size() > 0) {
            fileArrayList.remove(fileArrayList.size() - 1);
            // Notifying adapter that an item is removed.
            adapter.notifyItemRemoved(fileArrayList.size());
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
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared_files", MODE_PRIVATE);

        // creating a variable for editor to
        // store data in shared preferences.
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // creating a new variable for gson.
        Gson gson = new Gson();

        // getting data from gson and storing it in a string.
        String json = gson.toJson(fileArrayList);

        // below line is to save data in shared
        // prefs in the form of string.
        editor.putString(getString(R.string.files), json);

        // below line is to apply changes
        // and save data in shared prefs.
        editor.apply();

        // after saving data we are displaying a toast message.

    }
}