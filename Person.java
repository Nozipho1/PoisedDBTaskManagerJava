public class Person {

    // Field. Define attributes
    String name;
    String surname;
    String telephone;
    String email;
    String address;

    // Complete the constructor
    public Person(String name, String surname, String telephone, String email, String archAddress){
        this.name = name;
        this.surname = surname;
        this.telephone = telephone;
        this.email = email;
        this.address = archAddress;
    }

    // define getters and setters

    public String getName() {
        return name;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    // Define toString
    @Override
    public String toString() {
        return "\nName = " + name + '\n' +
                "Surname = " + surname + '\n' +
                "Telephone = " + telephone + '\n' +
                "Email = " + email + '\n' +
                "Address = " + address + '\n' +
                "";
    }

    public String toFile(){
        return name + "," + surname + "," + telephone + ","  + email + "," + address;
    }
}
