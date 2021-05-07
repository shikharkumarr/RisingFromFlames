package Model;

public class Sensor {
    String Name,Date,Status,ID;
    int Number;

    public Sensor(String Name, String ID, String Date, String Status, int Number){
        this.Name = Name;
        this.Date = Date;
        this.ID = ID;
        this.Status = Status;
        this.Number = Number;
    }

    public Sensor(){}

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getNumber() {
        return Number;
    }

    public void setNumber(int number) {
        Number = number;
    }
}
