package tn.esprit.tools;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class MyDb {

    public static final String url = "jdbc:mysql://localhost:3306/test1";
    public static final String user = "root";
    public static final String pwd = "";
    private Connection cnx;
    public static MyDb ct;

    private MyDb() {
        try {
            this.cnx = DriverManager.getConnection("jdbc:mysql://localhost:3306/test1", "root", "");
            System.out.println("connexion etablie !!");
        } catch (SQLException var2) {
            System.out.println(var2.getMessage());
        }

    }

    public Connection getCnx() {
        return this.cnx;
    }

    public static MyDb getInstance() {
        if (ct == null) {
            ct = new MyDb();
        }
        return ct;
    }
}
