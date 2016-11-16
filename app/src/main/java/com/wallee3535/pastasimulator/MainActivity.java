package com.wallee3535.pastasimulator;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
/*

 */
public class MainActivity extends AppCompatActivity {
    private ArrayList<Dish> dishes;
    private MainAdapter myAdapter;
    private ListView myListView;

    private boolean mIsBound = false;
    private MusicService mServ;

    private int selectedItemIndex = -1;

    private static final int REQ_CODE_ADD = 1;
    private static final int REQ_CODE_EDIT = 2;

    private ServiceConnection Scon = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder
                iBinder) {
            MusicService.ServiceBinder binder = (MusicService.ServiceBinder) iBinder;
            mServ = binder.getService();
            mServ.resumeMusic();
        }

        public void onServiceDisconnected(ComponentName name) {
            //mServ = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(Globals.TAG, "MainActivity-onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //bind to music service
        doBindService();

        //load data from database
        MyDatabaseHelper dbhelper = MyDatabaseHelper.getInstance(this);
        dishes = dbhelper.convertToArrayList();

        //create adapter and give it arraylist
        myAdapter = new MainAdapter(this, dishes);
        //get reference to listview
        myListView = (ListView) findViewById(R.id.dishListView);
        //link adapter to listview
        myListView.setAdapter(myAdapter);
        //handles clicks on each item and updates the selected item
        myListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        View clickedItem = myListView.getChildAt(i);
                        //update coloring information
                        deselect(selectedItemIndex);
                        select(i);
                        //update currently selected index
                        selectedItemIndex = i;
                    }
                }
        );
    }


    /** Binds to MusicService
     *
     */
    void doBindService() {
        if (!mIsBound) {
            bindService(new Intent(this, MusicService.class),
                    Scon, Context.BIND_AUTO_CREATE);
            mIsBound = true;
        }
    }

    /** Unbinds to MusicService
     *
     */
    void doUnbindService() {
        if (mIsBound) {
            mServ.pauseMusic();
            unbindService(Scon);
            mIsBound = false;
        }
    }

    /** Goes to EditDish activity and sets the mode to "add"
     * @param view
     */
    public void addButtonClicked(View view) {
        Log.i(Globals.TAG, "MainActivity-addButtonClicked");
        Intent i = new Intent(this, EditDish.class);
        i.putExtra("mode", "add");
        startActivityForResult(i, REQ_CODE_ADD);
    }

    /** Removes the selected item from the database and listview
     * @param view
     */
    public void deleteButtonClicked(View view) {
        if (selectedItemIndex == -1) {
            Toast.makeText(this, "must select a dish to delete", Toast.LENGTH_SHORT).show();
            Log.e(Globals.TAG, "tried to delete when no dish was selected");
            return;
        }

        try {
            Dish removedDish = dishes.remove(selectedItemIndex);
            selectedItemIndex = -1;
            myAdapter.notifyDataSetChanged();
            MyDatabaseHelper dbhelper = MyDatabaseHelper.getInstance(this);
            dbhelper.deleteDish(removedDish);
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.e("error", "tried to delete from dishes arraylist with invalid index");
        }

    }

    /** Removes all items from the database and listview
     * @param view
     */
    public void resetClicked(View view) {
        dishes.clear();
        myAdapter.notifyDataSetChanged();
        MyDatabaseHelper dbhelper = MyDatabaseHelper.getInstance(this);
        dbhelper.resetTable();
    }

    /** Goes to EditDish activity and sets the mode to "edit"
     * @param view
     */
    public void editButtonClicked(View view) {
        try {
            Dish dishToEdit = myAdapter.getItem(selectedItemIndex);
            Intent i = new Intent(this, EditDish.class);
            i.putExtra("mode", "edit");
            i.putExtra("prevType", dishToEdit.getType());
            i.putExtra("prevAmount", dishToEdit.getAmount());
            i.putExtra("prevUnit", dishToEdit.getUnit());


            startActivityForResult(i, REQ_CODE_EDIT);
        } catch (ArrayIndexOutOfBoundsException e) {
            Toast.makeText(this, "you need to select a dish to edit", Toast.LENGTH_SHORT).show();
            Log.e(Globals.TAG, "edit button clicked without selecting a dish");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            //get stored data
            String selectedType = data.getExtras().getString("selectedType");
            double enteredAmount = data.getExtras().getDouble("enteredAmount");
            String enteredUnit = data.getExtras().getString("enteredUnit");

            MyDatabaseHelper dbhelper = MyDatabaseHelper.getInstance(this);

            if (requestCode == REQ_CODE_ADD) {
                //add the dish to listview and database
                Dish dish = new Dish(selectedType, enteredAmount, enteredUnit);
                dishes.add(dish);
                myAdapter.notifyDataSetChanged();
                dbhelper.addDish(dish);
            } else if (requestCode == REQ_CODE_EDIT) {
                //edit the dish and update listview and database
                Dish dishToEdit = myAdapter.getItem(selectedItemIndex);
                dishToEdit.setType(selectedType);
                dishToEdit.setAmount(enteredAmount);
                dishToEdit.setUnit(enteredUnit);
                myAdapter.notifyDataSetChanged();
                dbhelper.updateDish(dishToEdit);
            }
        }
    }

    /** Colors the background of a child in the listview to mark as selected
     * @param index index of child to color as selected
     */
    public void select(int index) {
        try {
            View childView = myListView.getChildAt(index);
            childView.setBackgroundColor(Color.RED);
        } catch (NullPointerException e) {

        }
    }

    /** Colors the background of a child in the listview to mark as not selected
     * @param index index of child to color as not selected
     */
    public void deselect(int index) {
        try {
            View childView = myListView.getChildAt(index);
            childView.setBackgroundColor(Color.WHITE);
        } catch (NullPointerException e) {
        }
    }

    @Override
    protected void onDestroy() {
        doUnbindService();
        super.onDestroy();

    }


    @Override
    protected void onStop() {

        super.onStop();

    }

    @Override
    protected void onPause() {
        if(mServ!= null) mServ.pauseMusic();
        super.onPause();
    }

    @Override
    protected void onResume() {
        if(mServ!= null) mServ.resumeMusic();
        super.onResume();

    }
}
