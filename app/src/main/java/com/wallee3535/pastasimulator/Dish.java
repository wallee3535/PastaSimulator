package com.wallee3535.pastasimulator;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.Log;
import android.widget.GridLayout;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * Created by Walter on 7/16/2016.
 * Dish object
 */
public class Dish implements Serializable {
    //id used for deleting and updating database
    private long id;
    //type of pasta dish
    private String type;
    //amount of pasta
    private double amount;
    //unit of amount
    private String unit;

    public Dish() {
    }

    public Dish(long id, String type, double amount, String unit) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.unit = unit;
    }

    public Dish(String type, double amount, String unit) {
        this.type = type;
        this.amount = amount;
        this.unit = unit;
    }

    public String getAmountUnit() {
        return amount + " " + unit;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getMyDrawable(){
        int drawableId=-1;
        try {
            Class res = R.drawable.class;
            Field field = res.getField(type);
            drawableId = field.getInt(null);

        }
        catch (Exception e) {
            Log.e(Globals.TAG, "Failure to get drawable id.", e);
        }
        return drawableId;
    }
}
