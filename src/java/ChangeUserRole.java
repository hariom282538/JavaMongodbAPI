/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.mongodb.MongoClient;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author hariom
 */
@WebServlet(urlPatterns = {"/ChangeUserRole"})
public class ChangeUserRole extends HttpServlet {

    JSONObject error, exception, success;
    PrintWriter out;
    String status = null;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        try {
            out = response.getWriter();
            String id = request.getParameter("id");
            status = request.getParameter("type");
            System.out.println("id received : " + id);
            MongoClient mongoClient = new MongoClient("localhost", 27017);
            // Now connect to your databases
            DB db = mongoClient.getDB("XHiring");
            System.out.println("Connect to database successfully");
            DBCollection coll = db.getCollection("users");
            System.out.println("Collection users selected successfully");
            //read example
            try {
                BasicDBObject query = new BasicDBObject("_id", new ObjectId(id));
                BasicDBObject update;
                if (status != null && status.equalsIgnoreCase(" ")) {
                    update = new BasicDBObject("isAdmin", "true");
                } else {
                    update = new BasicDBObject("isAdmin", "false");
                }
                coll.update(query, update);
                success = new JSONObject();
                success.put("message", "document updated");
                out.println(success);
            } catch (NullPointerException ex) {
                error = new JSONObject();
                error.put("message", "No result found");
                out.println(error);
            }
            System.out.println("Document updated successfully");
        } catch (Exception e) {
            exception = new JSONObject();
            try {
                exception.put("message", "exception");
                out.println(exception);
            } catch (JSONException ex) {
                Logger.getLogger(ChangeUserRole.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }

    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
