package dhbkdn.it.dut.smartbot.models;

import java.io.Serializable;
import java.util.Date;

public class Equipment implements Serializable {
    private String name;
    private String status;
    private String alarm;
    private String image;
    private String device_status;
    private String id;


    public Equipment() {

    }

    public Equipment(String name, String status, String alarm, String image, String device_status, String id) {
        this.name = name;
        this.status = status;
        this.alarm = alarm;
        this.image = image;
        this.device_status = device_status;
        this.id = id;
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

    public String getDevice_status() {
        return device_status;
    }

    public void setDevice_status(String device_status) {
        this.device_status = device_status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Equipment{" +
                "name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", alarm='" + alarm + '\'' +
                ", image='" + image + '\'' +
                ", device_status='" + device_status + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
