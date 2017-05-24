package pt.webscraping.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by m_kac on 24.05.2017.
 */

public class SearchResult {
    public static Map<String, ArrayList<ProductView>> results = new HashMap<>();
    //public static ArrayList<ProductView> results = new ArrayList<>();
    public static String searchQuery;
    public static ArrayList<Template> templates;
    public static ArrayList<Template> selectedTemplates;
}
