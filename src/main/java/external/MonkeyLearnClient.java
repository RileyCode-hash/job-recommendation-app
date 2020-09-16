package external;
import com.monkeylearn.*;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class MonkeyLearnClient {
    private static final String API_KEY = "63c42c39fcc32e6b6c3f3f17ce48f93d04e9dbca";


    public static void main( String[] args ) throws MonkeyLearnException {

        String[] textList = {
                "Elon Musk has shared a photo of the spacesuit designed by SpaceX. This is the second image shared of the new design and the first to feature the spacesuit’s full-body look.",
                "Former Auburn University football coach Tommy Tuberville defeated ex-US Attorney General Jeff Sessions in Tuesday nights runoff for the Republican nomination for the U.S. Senate. ",
                "The NEOWISE comet has been delighting skygazers around the world this month – with photographers turning their lenses upward and capturing it above landmarks across the Northern Hemisphere."

        };

        List<List<String>> words = extraKeywords(textList);
        for (List<String> ws : words) {
            for (String w : ws) {
                System.out.println(w);
            }
            System.out.println();
        }
    }

     public static List<List<String>> extraKeywords(String[] text) {
        if (text == null || text.length == 0) {
            return new ArrayList<>();
        }
        // use api key from account
         MonkeyLearn ml = new MonkeyLearn(API_KEY);
        // use the keyword extractor

         ExtraParam[] extraParams = {new ExtraParam("max_keywords", "3")};
         MonkeyLearnResponse response;

         try {
             response = ml.extractors.extract("ex_YCya9nrn", text, extraParams);//change to your model id
             JSONArray resultArray = response.arrayResult;
             return getKeywords(resultArray);
         } catch (MonkeyLearnException e) {// it’s likely to have an exception
             e.printStackTrace();
         }
         return new ArrayList<>();
     }


    private static List<List<String>> getKeywords(JSONArray mlResultArray) {
         List<List<String>> topKeywords = new ArrayList<>();
         //iterate the result array and convert to our format
         for (int i = 0; i < mlResultArray.size(); i++) {
             List<String> keywords = new ArrayList<>();
             JSONArray keywordsArray = (JSONArray) mlResultArray.get(i);
             for (int j = 0; j < keywordsArray.size(); j++) {
                 JSONObject keywordObject = (JSONObject) keywordsArray.get(j);

             String keyword = (String) keywordObject.get("keyword");
             keywords.add(keyword);
              }
              topKeywords.add(keywords);
         }
         return topKeywords;
     }


}

