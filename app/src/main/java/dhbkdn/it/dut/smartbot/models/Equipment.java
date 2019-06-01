package dhbkdn.it.dut.smartbot.models;

import java.io.Serializable;
import java.util.Date;

public class Equipment implements Serializable {
    private String name;
    private String status;
    private String alarm;
    private String image;

    public Equipment() {

    }

    public Equipment(String name, String status, String alarm, String image) {
        this.name = name;
        this.status = status;
        this.alarm = alarm;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Equipment{" +
                "name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", alarm='" + alarm + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
