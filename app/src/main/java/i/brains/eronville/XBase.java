package i.brains.eronville;

public class XBase {

    String i, city, type, price, image, agent, line, whatsapp;
    double latitude, longitude;

    public XBase(String i, String city, String type, String price, String image, String agent, String line, String whatsapp, double latitude, double longitude) {
        this.i = i;
        this.city = city;
        this.type = type;
        this.price = price;
        this.image = image;
        this.agent = agent;
        this.latitude = latitude;
        this.longitude = longitude;
        this.line = line;
        this.whatsapp = whatsapp;
    }


    public String getI() {
        return i;
    }

    public String getCity() {
        return city;
    }

    public String getType() { return type; }

    public String getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public String getAgent() {
        return agent;
    }

    public String getLine() { return line; }

    public String getWhatsapp() { return whatsapp; }

    public double getLatitude() { return latitude; }

    public double getLongitude() { return longitude; }
}
