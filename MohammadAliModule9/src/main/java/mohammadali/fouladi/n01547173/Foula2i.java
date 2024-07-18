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

import com.google.android.material.snackbar.Snackbar;
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
import java.util.Objects;
// MohammadAli Fouladi N01547173

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
    private static final String SHARED_FILES = "shared_files";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ToggleButton fileType;
    private EditText fileName, fileContents;
    private LinearLayout fileNamesLayout;
    private RecyclerView fileRV;
    private FileAdapter adapter;
    private ArrayList<FileModal> fileArrayList;;
    private Snackbar snackbar;


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
                if (checkEmpty(fileName.getText().toString())) {
                    return;
                }
                replaceFile(getContext(), fileType.isChecked());


            }
        });

        view.findViewById(R.id.activity_internalstorage_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkEmpty(fileName.getText().toString())) {
                    return;
                }
                deleteFile(getContext(), fileType.isChecked());
            }
        });

        view.findViewById(R.id.activity_internalstorage_write).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkEmpty(fileName.getText().toString())) {
                    return;
                }
                if(checkEmpty(fileContents.getText().toString())){
                    Toast.makeText(getContext(), getString(R.string.moe)+"  "+getString(R.string.content_missing), Toast.LENGTH_LONG).show();
                }
                writeFile(getContext(), fileType.isChecked());
            }
        });

        view.findViewById(R.id.activity_internalstorage_read).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkEmpty(fileName.getText().toString())) {
                    return;
                }
                readFile(getContext(), fileType.isChecked());
            }
        });
        return view;
    }
    private void replaceFile(Context context, boolean isPersistent) {

        File directory = isPersistent ? context.getFilesDir() : context.getCacheDir();
        File[] files = directory.listFiles();
        if (files != null && files.length > 3) {
            // Find and delete the oldest file
            File oldestFile = files[0];
            for (File file : files) {
                if (file.lastModified() < oldestFile.lastModified()) {
                    oldestFile = file;
                }
            }
            oldestFile.delete();

            // Remove the oldest file from the list
            for (int i = 0; i < fileArrayList.size(); i++) {
                if (fileArrayList.get(i).getFileName().equals(oldestFile.getName())) {
                    fileArrayList.remove(i);
                    adapter.notifyItemRemoved(i);
                    saveData();
                    break;
                }
            }}
        else{ createFile(context, isPersistent);}

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
                replaceFile(context, isPersistent);

                saveData();



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

            fileContents.setText("");
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
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_FILES, MODE_PRIVATE);

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
        String fileNameToDelete = fileName.getText().toString();
        for (int i = 0; i < fileArrayList.size(); i++) {
            if (fileArrayList.get(i).getFileName().equals(fileNameToDelete)) {
                fileArrayList.remove(i);
                adapter.notifyItemRemoved(i);
                saveData();
                return;
            }

        }
    }
    private void saveData() {
        // method for saving the data in array list.
        // creating a variable for storing data in
        // shared preferences.
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_FILES, MODE_PRIVATE);

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
    boolean checkEmpty(String fileName){
        if (Objects.equals(fileName, ""))
        {
            snackbar = Snackbar.make(getView(), getString(R.string.moe)+"  "+getString(R.string.file_name_missing) , Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(R.string.dismiss, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            })        .show();
        return true;
            }
        else {
            return false;
    }
}
}