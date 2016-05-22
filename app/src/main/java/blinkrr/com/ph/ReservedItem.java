package blinkrr.com.ph;

public class ReservedItem {
    public String res_id, optprod_id, patient_id, start_date, end_date, qty;
    public Products prod;
    public OpticalShop opt_shop;

    public ReservedItem(String res_id, String optprod_id, String patient_id, String start_date, String end_date,
                        String qty, Products prod, OpticalShop opt_shop) {
        this.res_id = res_id;
        this.optprod_id = optprod_id;
        this.patient_id = patient_id;
        this.start_date = start_date;
        this.end_date = end_date;
        this.qty = qty;
        this.prod = prod;
        this.opt_shop = opt_shop;
    }
}
