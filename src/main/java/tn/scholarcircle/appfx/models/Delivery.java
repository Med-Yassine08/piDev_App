    package tn.scholarcircle.appfx.models;

    import java.time.LocalDate;

    public class Delivery {

        private int Id;
        private LocalDate Date;
        private String Address;
        private String Customer;
        private String Status;
        private int Quantity;
        private double Fees;
        private String NamePDelivery;

        public Delivery(int id, LocalDate date, String address, String customer, String status, int quantity, double Fees, String namePDelivery) {
            Id = id;
            Date = date;
            Address = address;
            Customer = customer;
            Status = status;
            Quantity = quantity;
            this.Fees = Fees;
            NamePDelivery = namePDelivery;
        }

        public Delivery(LocalDate date, String address, String customer, String status, int quantity, double Fees, String namePDelivery) {
            Date = date;
            Address = address;
            Customer = customer;
            Status = status;
            Quantity = quantity;
            this.Fees = Fees;
            NamePDelivery = namePDelivery;
        }

        public Delivery() {

        }

        public int getId() {
            return Id;
        }

        public void setId(int id) {
            Id = id;
        }

        public LocalDate getDate() {
            return Date;
        }

        public void setDate(LocalDate date) {
            Date = date;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String address) {
            Address = address;
        }

        public String getCustomer() {
            return Customer;
        }

        public void setCustomer(String customer) {
            Customer = customer;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            Status = status;
        }

        public int getQuantity() {
            return Quantity;
        }

        public void setQuantity(int quantity) {
            Quantity = quantity;
        }

        public double getFees() {
            return Fees;
        }

        public void setFees(double fees) {
            this.Fees = fees;
        }

        public String getNamePDelivery() {
            return NamePDelivery;
        }

        public void setNamePDelivery(String namePDelivery) {
            NamePDelivery = namePDelivery;
        }

        @Override
        public String toString() {
            return "Delivery{" +
                    "Id=" + Id +
                    ", Date=" + Date +
                    ", Address='" + Address + '\'' +
                    ", Customer='" + Customer + '\'' +
                    ", Status='" + Status + '\'' +
                    ", Quantity=" + Quantity +
                    ", fees=" + Fees +
                    ", NamePDelivery='" + NamePDelivery + '\'' +
                    '}';
        }
    }
