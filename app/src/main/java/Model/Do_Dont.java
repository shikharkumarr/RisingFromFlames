package Model;

public class Do_Dont {

    private String Text,Number,Circlebg,Details,Header;

    public Do_Dont(String Text, String Number, String Circlebg, String Details, String Header){
        this.Text = Text;
        this.Number = Number;
        this.Circlebg = Circlebg;
        this.Details = Details;
        this.Header = Header;
    }

    public Do_Dont(){}

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getCirclebg() {
        return Circlebg;
    }

    public void setCirclebg(String circlebg) {
        Circlebg = circlebg;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public String getHeader() {
        return Header;
    }

    public void setHeader(String header) {
        Header = header;
    }
}
