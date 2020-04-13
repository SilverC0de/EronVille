package i.brains.eronville;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class XChat extends RealmObject {
    @PrimaryKey String i;
    String image;
    String price;
    String details;
    String mail;
    String whatsapp;
    String line;
    String stamp;

    public String getI() {
        return i;
    }

    public void setI(String i) {
        this.i = i;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }
}