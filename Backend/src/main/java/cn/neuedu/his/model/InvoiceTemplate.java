package cn.neuedu.his.model;

import cn.neuedu.his.util.constants.Constants;

import java.util.ArrayList;

public class InvoiceTemplate {
    private Patient patient;
    private ConstantVariable settlementType;
    private Invoice invoice;
    private ArrayList<Payment> paymentList;

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public ConstantVariable getSettlementType() {
        return settlementType;
    }

    public void setSettlementType(ConstantVariable settlementType) {
        this.settlementType = settlementType;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public ArrayList<Payment> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(ArrayList<Payment> paymentList) {
        this.paymentList = paymentList;
    }
}
