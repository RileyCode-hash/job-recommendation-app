package rpc;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;

import db.MySQLConnection;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import db.MySQLTableCreation;
import entity.Item;

public class ItemHistory extends HttpServlet {
    public ItemHistory() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.setStatus(403);
            return;
        }

        String userId = req.getParameter("user_id");

        MySQLConnection connection = new MySQLConnection();
        Set<Item> items = connection.getFavoriteItems(userId);
        connection.close();

        JSONArray array = new JSONArray();
        for (Item item : items) {
            JSONObject obj = item.toJSONObject();
            obj.put("favorite", true);
            array.put(obj);
        }
        RpcHelper.writeJsonArray(resp, array);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.setStatus(403);
            return;
        }

        MySQLConnection connection = new MySQLConnection();
        JSONObject obj = new JSONObject(IOUtils.toString(req.getReader()));
        // step1:IOUtils.toString(req.getReader())
        String userId = obj.getString("user_id");
        Item item = RpcHelper.parseFavoriteItem(obj.getJSONObject("favorite"));

        connection.setFavoriteItems(userId, item);
        connection.close();
        RpcHelper.writeJsonObject(resp, new JSONObject().put("result", "SUCCESS"));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.setStatus(403);
            return;
        }
        MySQLConnection connection = new MySQLConnection();
        JSONObject input = new JSONObject(IOUtils.toString(req.getReader()));
        String userId = input.getString("user_id");
        Item item = RpcHelper.parseFavoriteItem(input.getJSONObject("favorite"));

        connection.unsetFavoriteItems(userId, item.getItemId());
        connection.close();
        RpcHelper.writeJsonObject(resp, new JSONObject().put("result", "SUCCESS"));

    }

}
