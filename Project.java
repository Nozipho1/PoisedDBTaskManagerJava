public class Project {

    // Field. Attributes are defined in the field
    String projNum;
    String projName;
    String buildingType;
    String address;
    String erfNum;
    double fee;
    double amountPaid;
    String deadline;

    Person architect;

    Person structuralEngineer;
    Person customer;

    String finalised;

    String dateFinalised;

    // Complete the constructor
    public Project(String projNum, String projName, String buildingType,String address,String erfNum,double fee, double amountPaid,String deadline,Person architect,Person structuralEngineer,Person customer,String finalised, String dateFinalised){
        this.projNum = projNum;
        this.projName = projName;
        this.buildingType = buildingType;
        this.address = address;
        this.erfNum = erfNum;
        this.fee = fee;
        this.amountPaid = amountPaid;
        this.deadline = deadline;
        this.architect = architect;
        this.structuralEngineer = structuralEngineer;
        this.customer = customer;
        this.finalised = finalised;
        this.dateFinalised = dateFinalised;
    }

    // define getters and setters

    public double getFee() {
        return fee;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public String getDeadline() {
        return deadline;
    }


    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public void setFinalised(String finalised) {
        this.finalised = finalised;
    }

    public void setDateFinalised(String dateFinalised) {
        this.dateFinalised = dateFinalised;
    }

    // define toString
    @Override
    public String toString() {
        return "\nProject:" + '\n' +
                "Project Number = " + projNum + '\n' +
                "Project Name = " + projName + '\n' +
                "Building Type = " + buildingType + '\n' +
                "Address = " + address + '\n' +
                "Erf Num = " + erfNum + '\n' +
                "Fee = " + fee + '\n' +
                "Amount Paid = " + amountPaid + '\n' +
                "Deadline = " + deadline + '\n' +
                "Architect: " + architect + '\n' +
                "Structural Engineer: " + structuralEngineer + '\n' +
                "Customer: " + customer + '\n' +
                "Status: " + finalised + '\n'+
                "Date Finalised: " + dateFinalised + '\n'+"";
    }
    public String toFile(){
        return projNum + "," + projName + "," + buildingType + "," + address + "," + erfNum + "," + fee + "," + amountPaid + "," + deadline + "," + architect.toFile() + "," + structuralEngineer.toFile() + "," + customer.toFile()+ "," + finalised + "\n";
    }

}
