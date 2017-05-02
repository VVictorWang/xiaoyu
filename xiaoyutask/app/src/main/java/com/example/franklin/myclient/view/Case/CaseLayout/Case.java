package com.example.franklin.myclient.view.Case.CaseLayout;

/**
 * Created by 小武哥 on 2017/4/23.
 */

public class Case {
  private String doctorName;
  private String illness;
  private String date;
  private String patientName;

  public void setPatientName(String patientName) {
    this.patientName = patientName;
  }

  public String getPatientName() {

    return patientName;
  }

  public Case(String patientName, String illness, String doctorName, String date) {
    this.doctorName = doctorName;
    this.patientName = patientName;
    this.illness = illness;
    this.date = date;
  }

  public String getDate() {
    return date;
  }

  public void setDoctorName(String doctorName) {
    this.doctorName = doctorName;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getIllness() {
    return illness;
  }

  public void setIllness(String illness) {
    this.illness = illness;
  }


  public String getDoctorName() {
    return doctorName;
  }


}
