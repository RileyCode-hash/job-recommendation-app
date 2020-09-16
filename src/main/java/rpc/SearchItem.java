package rpc;
import db.MySQLConnection;
import entity.Item;
import external.GitHubClient;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpSession;


public class SearchItem extends HttpServlet {
    private static final long serialVersionUID = 1l;

    public SearchItem() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();*/

       /* JSONArray array = new JSONArray();
        array.put(new JSONObject().put("username", "abcd"));
        array.put(new JSONObject().put("username", "1234"));
//        writer.print(array);
        RpcHelper.writeJsonArray(resp, array);*/
        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.setStatus(403);
            return;
        }
        String useId = req.getParameter("user_id");
        double lat = Double.parseDouble(req.getParameter("lat"));
        double lon = Double.parseDouble(req.getParameter("lon"));

        GitHubClient client = new GitHubClient();
        List<Item> items = client.search(lat, lon, null);

        MySQLConnection connection = new MySQLConnection();
        Set<String> favoritedItemIds = connection.getFavoriteItemIds(useId);
        connection.close();

        JSONArray array = new JSONArray();
        for(Item item : items) {
            JSONObject obj = item.toJSONObject();
            obj.put("favorite", favoritedItemIds.contains(item.getItemId()));
            array.put(obj);
        }
        RpcHelper.writeJsonArray(resp, array);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
