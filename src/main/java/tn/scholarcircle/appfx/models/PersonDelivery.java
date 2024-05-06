package tn.scholarcircle.appfx.models;

public class PersonDelivery {

    private int Id;
    private String FirstName;
    private String LastName;
    private String Address;
    private String Email;
    private Integer Phone;
    private String Sector;
    private String Transportation;

    public PersonDelivery(int id, String firstName, String lastName, String address, String email, Integer phone, String sector, String transportation) {
        Id = id;
        FirstName = firstName;
        LastName = lastName;
        Address = address;
        Email = email;
        Phone = phone;
        Sector = sector;
        Transportation = transportation;
    }

    public PersonDelivery(String firstName, String lastName, String address, String email, Integer phone, String sector, String transportation) {
        FirstName = firstName;
        LastName = lastName;
        Address = address;
        Email = email;
        Phone = phone;
        Sector = sector;
        Transportation = transportation;
    }

    public PersonDelivery() {

    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Integer getPhone() {
        return Phone;
    }

    public void setPhone(Integer phone) {
        Phone = phone;
    }

    public String getSector() {
        return Sector;
    }

    public void setSector(String sector) {
        Sector = sector;
    }

    public String getTransportation() {
        return Transportation;
    }

    public void setTransportation(String transportation) {
        Transportation = transportation;
    }

    @Override
    public String toString() {
        return "PersonDelivery{" +
                "Id=" + Id +
                ", FirstName='" + FirstName + '\'' +
                ", LastName='" + LastName + '\'' +
                ", Address='" + Address + '\'' +
                ", Email='" + Email + '\'' +
                ", Phone=" + Phone +
                ", Sector='" + Sector + '\'' +
                ", Transportation='" + Transportation + '\'' +
                '}';
    }


}
