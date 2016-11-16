package com.wallee3535.pastasimulator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by Walter on 7/16/2016.
 * Activity prompts user to select pasta type
 */
public class SelectPastaType extends AppCompatActivity{
    ArrayList<String> pastas;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_pasta_type);


        pastas = new ArrayList<String>();
        pastas.add("penne");
        pastas.add("linguine");

        final R.drawable drawableResources = new R.drawable();
        final Class<R.drawable> c = R.drawable.class;
        final Field[] fields = c.getDeclaredFields();

        for (int i = 0, max = fields.length; i < max; i++) {
            final int resourceId;
            try {
                resourceId = fields[i].getInt(drawableResources);
                String imageName = getResources().getResourceEntryName(resourceId);
                Log.i(Globals.TAG, "imageName: "+imageName);
            } catch (Exception e) {
                continue;
            }
        }


        SelectPastaTypeAdapter adapter = new SelectPastaTypeAdapter(this, pastas);
        ListView typeListView = (ListView) findViewById(R.id.typeListView);
        typeListView.setAdapter(adapter);

        typeListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("pastaType", pastas.get(i));
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }
                }
        );
    }
    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED, null);
        finish();
    }
}

