package blinkrr.com.ph;

public class Appointment{
    public String id, services, app_date, app_time, status;
    public OpticalShop opt_shop;

    public Appointment(String id, String services, String app_date, String app_time, String status, OpticalShop opt_shop) {
        this.id = id;
        this.services = services;
        this.app_date = app_date;
        this.app_time = app_time;
        this.status = status;
        this.opt_shop = opt_shop;
    }
}
