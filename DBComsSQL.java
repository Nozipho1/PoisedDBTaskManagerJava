import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DBComsSQL {


    static private Connection connection = null;
    static private Statement statement = null;
    static private int rowsAffected;
    static private ResultSet results;

    // define method to open a session
    public static ArrayList<Project> createDBSession(String url, String user, String password, String projectTable) throws SQLException {

        ArrayList<Project> arr = new ArrayList<>();

        connection = DriverManager.getConnection(
                url,
                user,
                password
        );
        // initialise statement variable
        statement = connection.createStatement();

        // Get values
        // create statements that allows user to access the rest of tables in database through foreign amd primary keys
        results = statement.executeQuery("Select * from " + projectTable + " inner join architectName on Projects.projNum = architectName.projNum inner join structuralEngineerName on Projects.projNum = structuralEngineerName.projNum inner join customerName on Projects.projNum = customerName.projNum");

        // while loop to loop through values of table in database
        while (results.next()) {

            // create person object for architect
            Person architect = new Person(results.getString("architectName.name"), results.getString("architectName.surname"), results.getString("architectName.telephone"), results.getString("architectName.email"), results.getString("architectName.address"));

            // create person object for structural engineer
            Person structuralEngineer = new Person(results.getString("structuralEngineerName.name"), results.getString("structuralEngineerName.surname"), results.getString("structuralEngineerName.telephone"), results.getString("structuralEngineerName.email"), results.getString("structuralEngineerName.address"));

            // create person object for customer
            Person customer = new Person(results.getString("customerName.name"), results.getString("customerName.surname"), results.getString("customerName.telephone"), results.getString("customerName.email"), results.getString("customerName.address"));

            Project projectX = new Project(results.getString("projNum"), results.getString("projName"), results.getString("buildingType"), results.getString("address"), results.getString("erfNum"), results.getFloat("fee"), results.getFloat("amountPaid"), results.getString("deadline"), architect, structuralEngineer, customer, results.getString("Finalised"), results.getString("dateFinalised"));

            // append objects to project array list
            arr.add(projectX);
        }
        return arr;

    }

    // define method to connect to the database
    public static Connection createSession(String url, String user, String password) {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (Exception ex) {
            System.out.println("couldn't connect!");
            throw new RuntimeException(ex);
        }
    }

    public static void printAllFromTable(Statement statement) throws
            SQLException {
        ResultSet results = statement.executeQuery("SELECT projNum, projName, buildingType, address, erfNum, fee, amountPaid, deadline, architectName, structuralEngineerName, customerName, Finalised, dateFinalised FROM Projects");

        // while loop to loop through values of table in database
        while (results.next()) {
            System.out.println(
                    results.getString("projNum") + ", "
                            + results.getString("projName") + ", "
                            + results.getString("buildingType") + ", "
                            + results.getString("address") + ", "
                            + results.getString("erfNum") + ", "
                            + results.getFloat("fee") + ", "
                            + results.getFloat("amountPaid") + ", "
                            + results.getString("deadline") + ", "
                            + results.getString("architectName") + ", "
                            + results.getString("structuralEngineerName") + ", "
                            + results.getString("customerName") + ", "
                            + results.getString("Finalised") + ", "
                            + results.getString("dateFinalised")
            );
        }
    }

    // define method to edit deadline
    static void editDueDate(String url, String user, String password, String projNumber, String deadline) throws SQLException {

        // declare and initialise connection statement
        int rowsUpdatedValue;
        Connection conStatement = createSession(url, user, password);

        // declare and initialise result set statement
        ResultSet results = statement.executeQuery("SELECT deadline FROM Projects");

        // while loop to loop over results in the database and retrieve results
        while (results.next()) {
            PreparedStatement myStmt = conStatement.prepareStatement("UPDATE Projects SET deadline =? WHERE projNum=?");

            myStmt.setString(1, deadline);
            myStmt.setString(2, projNumber);

            rowsUpdatedValue = myStmt.executeUpdate();
            // Inform user that the update was successful
            System.out.println("Query complete, " + rowsUpdatedValue + " rows updated.");


        }
    }

    // define method to update project status
    public static void updateProjectStatus(String url, String user, String password, String projNumber) throws SQLException {
        // create an LocalDate object
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedString = today.format(formatter);
        System.out.println("this is the current date " + today);

        int rowsUpdatedValue;
        Connection conStatement = createSession(url, user, password);

        ResultSet results = statement.executeQuery("SELECT Finalised FROM Projects");

        // conditional body to check whether status is finalised or not
        while (results.next()) {
            if (results.getString("Finalised").equalsIgnoreCase("Finalised")) {
                // if project is finalised then they can't select it to edit.
                continue;
            } else {
                try {
                    //TODO
                    // the problem is the way we passing in the statement it takes multiple statement thorugh comma
                    PreparedStatement myStmt = conStatement.prepareStatement("UPDATE Projects SET Finalised=?, dateFinalised=? WHERE projNum=?");
                    myStmt.setString(1, "Finalised");
                    myStmt.setString(2, formattedString);
                    myStmt.setString(3, projNumber);

                    rowsUpdatedValue = myStmt.executeUpdate();
                    System.out.println("Query complete, " + rowsUpdatedValue + " rows updated.");

                } catch (Exception e) {
                    System.out.println(e);
                    System.out.println("An error occurred while making update, " + rowsAffected + "rows updated");
                }

                try {
                    results = statement.executeQuery("SELECT * FROM Projects Where projNum=" + projNumber);
                    while (results.next()) {

                        String customerName = results.getString("customerName");
                        Float feeAmount = results.getFloat("fee");
                        Float amountPaid = results.getFloat("amountPaid");

                        // conditional body to generate invoice
                        if (feeAmount > amountPaid) {
                            double outstanding = feeAmount - amountPaid;
                            System.out.println("Dear " + customerName + " You still have R" + outstanding + " left to pay\n\n");
//                            rowsUpdatedValue = statement.executeQuery();
                            System.out.println("Query complete, " + results + " rows updated.");

                        } else {
                            System.out.println("Project has been finalised and Fee has been paid in full.");
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }

            }
        }
    }


    // define method to view all complete tasks
    public static void viewComplete(String url, String user, String password) throws SQLException {

        createSession(url, user, password);

        ResultSet results = statement.executeQuery("SELECT Finalised FROM Projects");

        // looping through the database to capture results
        while (results.next()) {
            if (results.getString("Finalised").equalsIgnoreCase("Finalised")) {
                // printing all completed tasks
                System.out.println(results);
            }
        }
    }

    // define method to view all incomplete projects
    public static void viewIncomplete(String url, String user, String password) throws SQLException {

        createSession(url, user, password);

        ResultSet results = statement.executeQuery("SELECT Finalised FROM Projects");

        while (results.next()) {
            if (results.getString("Finalised").equalsIgnoreCase("Incomplete")) {
                // if project is finalised then they can't select it to edit.
                System.out.println(results);
            }
        }
    }

    // define method to check whether project is past deadline
    public static void pastDeadline(String url, String user, String password) throws SQLException {
        // create an LocalDate object
        LocalDate today = LocalDate.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        ResultSet results = statement.executeQuery("SELECT * FROM Projects");

        LocalDate localDate;

        // while loop to loop through database and store specified results
        try {
            while (results.next()) {
                String deadlineDate = results.getString("deadline");
                String projStatus = results.getString("Finalised");
                localDate = LocalDate.parse(deadlineDate, format);

                // conditional body to check where project is past deadline or not
                if ((localDate.isBefore(today)) && (projStatus.equalsIgnoreCase("Incomplete"))) {
                    System.out.println(
                            results.getString("projNum") + ", "
                                    + results.getString("projName") + ", "
                                    + results.getString("buildingType") + ", "
                                    + results.getString("address") + ", "
                                    + results.getString("erfNum") + ", "
                                    + results.getFloat("fee") + ", "
                                    + results.getFloat("amountPaid") + ", "
                                    + results.getString("deadline") + ", "
                                    + results.getString("architectName") + ", "
                                    + results.getString("structuralEngineerName") + ", "
                                    + results.getString("customerName") + ", "
                                    + results.getString("Finalised") + ", "
                                    + results.getString("dateFinalised")
                    );
                }
            }
            System.out.println("There are no projects past deadline.");

        } catch (Exception e) {
            System.out.println(e);
        }


    }

    // define a method to add project into database
    public static void insertDB(String url, String user, String password, Project tempProject) throws SQLException {

        int rowsUpdatedValue;

        Connection conStatement = createSession(url, user, password);

        try {
            // use prepared statements to insert values
            PreparedStatement myStmt = conStatement.prepareStatement("INSERT INTO Projects VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)"); // this worked after spaces were fixed
            myStmt.setString(1, tempProject.projNum);
            myStmt.setString(2, tempProject.projName);
            myStmt.setString(3, tempProject.buildingType);
            myStmt.setString(4, tempProject.address);
            myStmt.setString(5, tempProject.erfNum);
            myStmt.setFloat(6, (float) tempProject.fee);
            myStmt.setFloat(7, (float) tempProject.amountPaid);
            myStmt.setString(8, tempProject.deadline);
            myStmt.setString(9, tempProject.architect.name);
            myStmt.setString(10, tempProject.structuralEngineer.name);
            myStmt.setString(11, tempProject.customer.name);
            myStmt.setString(12, tempProject.finalised);
            myStmt.setString(13, "n/a");

            rowsUpdatedValue = myStmt.executeUpdate();
            System.out.println(rowsUpdatedValue + " records inserted into projects");


            // section focuses on architect details to architect table
            myStmt = connection.prepareStatement("INSERT INTO architectName VALUES (?,?,?,?,?,?)");
            myStmt.setString(1, tempProject.projNum);
            myStmt.setString(2, tempProject.architect.name);
            myStmt.setString(3, tempProject.architect.surname);
            myStmt.setString(4, tempProject.architect.telephone);
            myStmt.setString(5, tempProject.architect.email);
            myStmt.setString(6, tempProject.architect.address);

            // rows updated
            rowsUpdatedValue = myStmt.executeUpdate();
            // printing out the number of rows updated
            System.out.println(rowsUpdatedValue + " record inserted into arch table");

            // section focuses on structural engineer details into struct table
            myStmt = connection.prepareStatement("INSERT INTO structuralEngineerName VALUES (?,?,?,?,?,?)");
            myStmt.setString(1, tempProject.projNum);
            myStmt.setString(2, tempProject.structuralEngineer.name);
            myStmt.setString(3, tempProject.structuralEngineer.surname);
            myStmt.setString(4, tempProject.structuralEngineer.telephone);
            myStmt.setString(5, tempProject.structuralEngineer.email);
            myStmt.setString(6, tempProject.structuralEngineer.address);

            // rows updated
            rowsUpdatedValue = myStmt.executeUpdate();
            // printing out the number of rows updated
            System.out.println(rowsUpdatedValue + " record inserted into struct table");

            // section focuses on customer details into customer table
            myStmt = connection.prepareStatement("INSERT INTO customerName VALUES (?,?,?,?,?,?)");  // same issue here, extra comma and space
            myStmt.setString(1, tempProject.projNum);
            myStmt.setString(2, tempProject.customer.name);
            myStmt.setString(3, tempProject.customer.surname);
            myStmt.setString(4, tempProject.customer.telephone);
            myStmt.setString(5, tempProject.customer.email);
            myStmt.setString(6, tempProject.customer.address);

            // rows updated
            rowsUpdatedValue = myStmt.executeUpdate();
            // printing out the number of rows updated
            System.out.println(rowsUpdatedValue + " record inserted into customer table");

        } catch (Exception e) {
            System.out.println(e);
        }
    }


    // delete a project
    public static void deleteDB(String url, String user, String password, String tableName, String whereVal) throws
            SQLException {
        Connection conStatement = DriverManager.getConnection(url, user, password);
        Statement statement = conStatement.createStatement();

        try {
            rowsAffected = statement.executeUpdate("delete from " + tableName + " Where projNum= " + whereVal);
            System.out.println("Deletion successful " + rowsAffected + "records edited");
        } catch (SQLException sqlE) {
            System.out.println(sqlE);
            System.out.println("An error occured while making update, " + rowsAffected + "rows updated");
        }

        connection.close();
        statement.close();
    }

    // we can call this method since it return a person object then we just call the person's edit method in the main program.
    // we'll pass the person type to avoid code verbosity
    public static Person getPerson(String url, String user, String password, String tableName, String projectNum) throws
            SQLException {

        Connection conStatement = DriverManager.getConnection(url, user, password);
        Statement statement = conStatement.createStatement();

        ResultSet results = statement.executeQuery("Select * from " + tableName + " where projNum=" + projectNum);


        Person searchPerson = null;

        try {
            while (results.next()) {
                // get person information and create object
                System.out.println(
                        results.getString("projNum") + ", "
                                + results.getString("Name") + ", "
                                + results.getString("surname") + ", "
                                + results.getString("telephone") + ", "
                                + results.getString("email") + ", "
                                + results.getString("address"));

                searchPerson = new Person(results.getString("Name"), results.getString("surname"), results.getString("telephone"), results.getString("email"), results.getString("address"));
                // assign searchPerson the value of object
                System.out.println(results + " record successfully edited");

            }
            statement.close();
            results.close();

        } catch (Exception e) {
            System.out.println(e);
        }
        return searchPerson;
    }

    // define method to Update person details
    public static void updatePersonDetails(String url, String user, String password, Person personObj, String
            tableName, String projNumber) throws SQLException {
        // deconstruct the person object to ensure it is sent to the database as needed.
        // set validation to move around the type of person who'll be updated.
        Connection conStatement = DriverManager.getConnection(url, user, password);

        int rowsUpdatedValue;
        PreparedStatement myStmt = null;

        try {
            // setting the new person's updated details to the table
            if (tableName.equalsIgnoreCase("structuralengineername")) {
                myStmt = conStatement.prepareStatement("UPDATE structuralEngineername SET name=?, surname=?, telephone=?, email=?, address=? WHERE projNum=?");

            } else if (tableName.equalsIgnoreCase("architectname")) {
                myStmt = conStatement.prepareStatement("UPDATE architectName SET name=?, surname=?, telephone=?, email=?, address=? WHERE projNum=?");
            } else if (tableName.equalsIgnoreCase("customername")) {
                myStmt = conStatement.prepareStatement("UPDATE customerName SET name=?, surname=?, telephone=?, email=?, address=? WHERE projNum=?");
            }

            myStmt.setString(1, personObj.name);
            myStmt.setString(2, personObj.surname);
            myStmt.setString(3, personObj.telephone);
            myStmt.setString(4, personObj.email);
            myStmt.setString(5, personObj.address);
            myStmt.setString(6, projNumber);


            rowsUpdatedValue = myStmt.executeUpdate();
            System.out.println("Query complete, " + rowsUpdatedValue + " rows updated.");

        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
