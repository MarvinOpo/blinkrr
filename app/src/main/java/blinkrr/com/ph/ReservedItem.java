package blinkrr.com.ph;

public class ReservedItem {
    public String res_id, optprod_id, patient_id, start_date, end_date;

    public ReservedItem(String res_id, String optprod_id, String patient_id, String start_date, String end_date) {
        this.res_id = res_id;
        this.optprod_id = optprod_id;
        this.patient_id = patient_id;
        this.start_date = start_date;
        this.end_date = end_date;
    }
}
