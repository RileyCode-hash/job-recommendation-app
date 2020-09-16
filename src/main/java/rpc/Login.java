package rpc;

import db.MySQLConnection;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class Login extends HttpServlet {
    private static final long serialVersionUID = 1l;

    public Login() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        JSONObject obj = new JSONObject();
        if (session != null) {
            MySQLConnection connection = new MySQLConnection();
            String userId = session.getAttribute("user_id").toString();
            obj.put("status", "ok").put("user_id", userId).put("name", connection.getFullname(userId));
            connection.close();
        } else {
            obj.put("status", "Invalid Session");
            resp.setStatus(403);
        }
        RpcHelper.writeJsonObject(resp, obj);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject input = new JSONObject(IOUtils.toString(req.getReader()));
        String userId = input.getString("user_id");
        String password = input.getString("password");

        MySQLConnection connection = new MySQLConnection();
        JSONObject obj = new JSONObject();
        if (connection.verifyLogin(userId, password)) {
            HttpSession session = req.getSession();
            session.setAttribute("user_id", userId);
            obj.put("status", "ok").put("user_id", userId).put("name", connection.getFullname(userId));
        } else {
            obj.put("status", "Login failed, user id and passcode do not exist.");
            resp.setStatus(401);
        }
        connection.close();
        RpcHelper.writeJsonObject(resp, obj);

    }
}
