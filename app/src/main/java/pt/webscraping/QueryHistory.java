package pt.webscraping;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by szymon on 20.04.2017.
 */

public class QueryHistory {

    private final static String tag = "queryHistory";
    private final static Gson gson = new Gson();

    private static void set(Context ctx, ArrayList<String> queryHistory){
        SharedPreferences sharedPref = ctx.getSharedPreferences(
                ctx.getString(R.string.shared_preferenced_key), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();

        String newArray = gson.toJson(queryHistory);

        editor.putString(tag, newArray);
        editor.commit();
    }

    public static ArrayList<String> get(Context ctx){
        SharedPreferences sharedPref = ctx.getSharedPreferences(
                ctx.getString(R.string.shared_preferenced_key), Context.MODE_PRIVATE);

        String json = sharedPref.getString(tag, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();

        ArrayList<String> queryHistory = gson.fromJson(json, type);

        if(queryHistory == null){
            return new ArrayList<String>();
        }
        return queryHistory;
    }

    public static void add(Context ctx, String query){

        ArrayList<String> queries = get(ctx);
        if(queries.size() == 10){
            queries.remove(0);
        }
        queries.add(query);
        set(ctx, queries);
    }
}
