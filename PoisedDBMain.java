import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class PoisedDBMain {

	// Define static/global variables
	static Scanner sc = new Scanner(System.in);
	static ArrayList<Project> projectArrayList;

	//	creating variables to log in with to reduce repeated code
	static String userUrl = "jdbc:mysql://localhost:3306/Poised_db?useSSL=false";
	static String userLogin = "root";
	static String userPassword = "bunniez1";

	static {
		try {
			projectArrayList = DBComsSQL.createDBSession(
					userUrl,
					userLogin,
					userPassword,
					"Projects");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	static ArrayList<Project> projDetails = new ArrayList<>();

	// Define main function
	public static void main(String[] args) throws IOException, ParseException, SQLException {
		Project currentProject;

		String userChoice;
		while (true) {

			// Request input from the user and store the response in a variable
			System.out.println("1. Create new Project\n2. Update Existing Project\n3. View all tasks\n4. Exit");
			String menuChoice = sc.nextLine();

			// Call relevant function based on user's input

			if (menuChoice.equals("1")) {
				currentProject = createProject();
				DBComsSQL.insertDB(userUrl,
						userLogin,
						userPassword, currentProject);

				// append details of created project into list
				projDetails.add(currentProject);
				System.out.println(projDetails);

			} else if (menuChoice.equals("2")) {
				while (true) {
					// selecting project by number
					System.out.println("Select 1 to edit project or enter 2 to delete a project or enter -1 to exit: ");
					String userTaskPosition = sc.nextLine();

					if (userTaskPosition.equalsIgnoreCase("2")) {
						System.out.println("Enter Table Name: ");
						String tableName = sc.nextLine();
						System.out.println("Enter Project Number: ");
						String whereVal = sc.nextLine();

						// Call relevant function based on user's input
						DBComsSQL.deleteDB(userUrl,
								userLogin, userPassword, tableName, whereVal);
					}

					if (!userTaskPosition.equalsIgnoreCase("-1")) {

						System.out.println("Enter Project number: ");
						String projNum = sc.nextLine();

						// if project  is found then display menu and save user input
						System.out.println("1. Change Deadline\n2. Update Person Details\n3. Finalize Project\n6. exit\nSelection:");

						String editChoice = sc.nextLine();

						// based on user input call the relevant function
						if (editChoice.equals("1")) {
							System.out.println("Enter New deadline(dd/mm/yyyy): ");
							String newDeadline = sc.nextLine();

							// Call relevant function based on user's input
							DBComsSQL.editDueDate(userUrl, userLogin, userPassword, projNum, newDeadline);

							break;
						} else if (editChoice.equals("2")) {
							// sub menu to edit specific person's details, you will need to create methods to push updates on these people's objects
							// then
							System.out.println("1. Update Structural Engineer Information \n2. Update Architect Information \n3. Update Customer Information");
							String personEditChoice = sc.nextLine();

							if (personEditChoice.equals("1")) {

								// Call relevant function based on user's input
								Person structEngineer = DBComsSQL.getPerson(userUrl,
										userLogin, userPassword, "structuralEngineerName", projNum);

								Person updatedStruc = updateStructuralEngineer(structEngineer);
								String tableName = "structuralEngineerName";

								// Call relevant function based on user's input
								DBComsSQL.updatePersonDetails(userUrl, userLogin, userPassword, updatedStruc, tableName, projNum);

								break;
							} else if (editChoice.equals("2")) {
//							updateArchitect(proj);

								// Call relevant function based on user's input
								Person arch = DBComsSQL.getPerson(userUrl,
										userLogin, userPassword, "architectName", projNum);

								Person updatedArch = updateArchitect(arch);
								String tableName = "architectName";

								// Call relevant function based on user's input
								DBComsSQL.updatePersonDetails(userUrl, userLogin, userPassword, updatedArch, tableName, projNum);
								break;
							} else if (editChoice.equals("3")) {

								// Call relevant function based on user's input
								Person customer = DBComsSQL.getPerson(userUrl,
										userLogin, userPassword, "customerName", projNum);

								Person updatedCustomer = updateCustomer(customer);
								String tableName = "customerName";

								// Call relevant function based on user's input
								DBComsSQL.updatePersonDetails(userUrl, userLogin, userPassword, updatedCustomer, tableName, projNum);
								break;
							}
						} else if (editChoice.equals("3")) {
							// moved the database update into this section because this method only updates project details.
							DBComsSQL.updateProjectStatus(userUrl,
									userLogin, userPassword, projNum);
							break;
						}

						if (editChoice.equals("6")) {
							System.out.println("Going back to main");
							break;
						}
					} else {
						break;
					}
				}
			} else if (menuChoice.equals("3")) {
				System.out.println("Press 1 to view all incomplete tasks and 2 to view all complete tasks and 3 to view all tasks past deadline.");
				userChoice = sc.nextLine();

				// Call relevant function based on user's input
				if (userChoice.equals("1")) {
					DBComsSQL.viewIncomplete(userUrl, userLogin, userPassword);

				}
			 else if (userChoice.equals("2")) {

					// Call relevant function based on user's input
				DBComsSQL.viewComplete(userUrl, userLogin, userPassword);
			} else if (userChoice.equals("3")) {

					// Call relevant function based on user's input
					DBComsSQL.pastDeadline(userUrl, userLogin, userPassword);
				}

			} else if (menuChoice.equals("4")) {
				break;
			}
		}
	}

	// define method to create project
	private static Project createProject() throws SQLException {
		System.out.println("Project number: ");
		String projNum = sc.nextLine();
		System.out.println("Project name: ");
		String projName = sc.nextLine();
		System.out.println("Building type: ");
		String buildingType = sc.nextLine();
		System.out.println("Building address: ");
		String buildingAddress = sc.nextLine();
		System.out.println("ERF number: ");
		String erf = sc.nextLine();

		// try catch loop to accommodate number format exceptions
		// declare variables outside scope of loop
		double totalFee;
		while (true) {
			try {
				System.out.println("Total fee: ");
				totalFee = Double.parseDouble(sc.nextLine());
				break;
			} catch (NumberFormatException e) {
				System.out.println("Please enter an appropriate digit: ");
			}
		}
		double totalPaid;
		while (true) {
			try {
				System.out.println("Total amount paid to date: ");
				totalPaid = Double.parseDouble(sc.nextLine());
				break;
			} catch (NumberFormatException e) {
				System.out.println("Please enter an appropriate digit: ");
			}
		}

		String deadline;
		while (true) {
			try {
				System.out.println("Deadline(d/MM/yyyy): ");
				deadline = sc.nextLine();
				break;
			} catch (DateTimeParseException e) {
				System.out.println("Please enter appropriate format");
			}
		}

		// Create architect, structuralEngineer and customer objects
		System.out.println("Architect details: ");
		Person architect = createPerson();
		System.out.println("Structural Engineer details: ");
		Person structuralEngineer = createPerson();
		System.out.println("Customer details: ");
		Person customer = createPerson();
		String finalised = "Incomplete";
		String dateFinalised = "n/a";
		// Create project object using information gathered from user and person object
		// Add project object
		if (projName.isEmpty()) {
			projName = buildingType + " " + customer.surname;
		}


		return new Project(projNum, projName, buildingType, buildingAddress, erf, totalFee, totalPaid, deadline, architect, structuralEngineer, customer, finalised, dateFinalised);
	}

	/**
	 * create person method.
	 * The method asks the user to input details of a specific person and stores information in designated variables
	 *
	 * @return object variable to be printed
	 */
	private static Person createPerson() {
		// Get input from the user
		System.out.println("Name: ");
		String name = sc.nextLine();
		System.out.println("Surname: ");
		String surname = sc.nextLine();
		System.out.println("Telephone number: ");
		String tel = sc.nextLine();
		System.out.println("Email address: ");
		String email = sc.nextLine();
		System.out.println("Physical address: ");
		String address = sc.nextLine();
		// Create A person object for architect, contractor and customer
		return new Person(name, surname, tel, email, address);
	}

	/**
	 * create edit due date method.
	 * The method asks the user to input updated details of a specific due date and stores information in designated variables
	 *
	 * @param projectObj object to be edited
	 */


	/**
	 * create edit contractor information method.
	 * The method asks the user to input updated details of the contractor and stores information in designated variables
	 *
	 * @param personObj object to be edited
	 */
	private static Person updateStructuralEngineer(Person personObj) {

		// Request option from user and store in a variable
		System.out.println("1. Change Email\n2. Change Tell NO\n3. Change Physical Address\n4. exit\nSelection: ");
		String structuralEngineerEditChoice = sc.nextLine();

		// Based on user choice, request the relevant information from user and user relevant setters to change values
		// conditional body to update information

		if (structuralEngineerEditChoice.equals("1")) {
			System.out.println("Updated email address: ");
			String newEmail = sc.nextLine();
			personObj.setEmail(newEmail);
			System.out.println(personObj);
			// Inform user that the update was successful
			System.out.println("Update success.");

		} else if (structuralEngineerEditChoice.equals("2")) {
			System.out.println("Updated telephone number: ");
			String newTel = sc.nextLine();
			personObj.setTelephone(newTel);
			System.out.println(personObj);
			// Inform user that the update was successful
			System.out.println("Update success.");

		} else if (structuralEngineerEditChoice.equals("3")) {
			System.out.println("Updated address: ");
			String newAddress = sc.nextLine();
			personObj.setAddress(newAddress);

		} else if (structuralEngineerEditChoice.equals("4")) {
			System.out.println("Going to main");
		} else {
			System.out.println("Incorrect option");
		}
		return personObj;
	}

	/**
	 * create edit architect information method.
	 * The method asks the user to input updated details of the architect and stores information in designated variables
	 *
	 * @param personObj object to be edited
	 * @return
	 */
	private static Person updateArchitect(Person personObj) {

		// Request option from user and store in a variable
		System.out.println("1. Change Email\n2. Change Tell NO\n3. Change Physical Address\n4. exit\nSelection: ");
		String architectEditChoice = sc.nextLine();

		// Based on user choice, request the relevant information from user and user relevant setters to change values
		// conditional body to update information

		if (architectEditChoice.equals("1")) {
			System.out.println("Updated email address: ");
			String newEmail = sc.nextLine();
			personObj.setEmail(newEmail);
			System.out.println(personObj);
			// Inform user that the update was successful
			System.out.println("Update success.");

		} else if (architectEditChoice.equals("2")) {
			System.out.println("Updated telephone number: ");
			String newTel = sc.nextLine();
			personObj.setTelephone(newTel);
			System.out.println(personObj);
			// Inform user that the update was successful
			System.out.println("Update success.");

		} else if (architectEditChoice.equals("3")) {
			System.out.println("Updated address: ");
			String newAddress = sc.nextLine();
			personObj.setAddress(newAddress);

		} else if (architectEditChoice.equals("4")) {
			System.out.println("Going to main");
		} else {
			System.out.println("Incorrect option");
		}
		return personObj;
	}

	/**
	 * create edit customer information method.
	 * The method asks the user to input updated details of the customer and stores information in designated variables
	 *
	 * @param personObj object to be edited
	 * @return
	 */
	private static Person updateCustomer(Person personObj) {

		// Request option from user and store in a variable
		System.out.println("1. Change Email\n2. Change Tell NO\n3. Change Physical Address\n4. exit\nSelection: ");
		String customerEditChoice = sc.nextLine();

		// Based on user choice, request the relevant information from user and user relevant setters to change values
		// conditional body to update information

		if (customerEditChoice.equals("1")) {
			System.out.println("Updated email address: ");
			String newEmail = sc.nextLine();
			personObj.setEmail(newEmail);
			System.out.println(personObj);
			// Inform user that the update was successful
			System.out.println("Update success.");

		} else if (customerEditChoice.equals("2")) {
			System.out.println("Updated telephone number: ");
			String newTel = sc.nextLine();
			personObj.setTelephone(newTel);
			System.out.println(personObj);
			// Inform user that the update was successful
			System.out.println("Update success.");

		} else if (customerEditChoice.equals("3")) {
			System.out.println("Updated address: ");
			String newAddress = sc.nextLine();
			personObj.setAddress(newAddress);

		} else if (customerEditChoice.equals("4")) {
			System.out.println("Going to main");
		} else {
			System.out.println("Incorrect option");
		}
		return personObj;
	}

	/**
	 * create edit finalise project information method.
	 * The method asks the user to input updated details of a specific
	 * project's details and stores information in designated variables
	 * method will either produce an invoice that is to be written to the invoice.txt file
	 * if project has not been paid up in full
	 * or the project will be marked as finalised
	 *
	 * @param projectObj object to be edited
	 */

	}

	/**
	 * create fileWriter method.
	 * initialises an instance of the file and then loops through the list and writes
	 * values to PoisedProjects.txt file
	 */


	/**
	 * create fileReader method.
	 * initialises an instance of the file and then uses scanner object to loop through file
	 * to read from PoisedProjects.txt file.
	 * appends project object to array list
	 */
