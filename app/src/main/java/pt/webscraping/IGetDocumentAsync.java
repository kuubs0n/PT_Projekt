package pt.webscraping;

import org.jsoup.nodes.Document;

/**
 * Created by szymon on 11.04.2017.
 */

public interface IGetDocumentAsync {
    void process(Document doc);
}