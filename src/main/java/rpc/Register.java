package rpc;

import db.MySQLConnection;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Register extends HttpServlet {
    private static final long serialVersionUID = 1L;


    public Register() {

        super();
    }


    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        JSONObject input = new JSONObject(IOUtils.toString(request.getReader()));
        String userId = input.getString("user_id");
        String password = input.getString("password");
        String firstname = input.getString("first_name");
        String lastname = input.getString("last_name");

        MySQLConnection connection = new MySQLConnection();
        JSONObject obj = new JSONObject();
        if (connection.addUser(userId, password, firstname, lastname)) {
            obj.put("status", "ok");
        } else {
            obj.put("status", "User Already Exists");
        }
        connection.close();
        RpcHelper.writeJsonObject( response, obj);

    }
}
