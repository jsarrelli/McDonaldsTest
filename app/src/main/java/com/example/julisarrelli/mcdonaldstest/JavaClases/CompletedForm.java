package com.example.julisarrelli.mcdonaldstest.JavaClases;

/**
 * Created by julisarrelli on 1/16/17.
 */
public class CompletedForm {
    private int idForm;
    private String name;
    private int forms_idForm;
    private String locals_adress;
    private String users_username;
    private String date;

    public CompletedForm(int idForm, String name, int forms_idForm, String locals_adress, String users_username, String date) {
        this.idForm = idForm;
        this.name = name;
        this.forms_idForm = forms_idForm;
        this.locals_adress = locals_adress;
        this.users_username = users_username;
        this.date = date;
    }


    public int getIdForm() {
        return idForm;
    }

    public void setIdForm(int idForm) {
        this.idForm = idForm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getForms_idForm() {
        return forms_idForm;
    }

    public void setForms_idForm(int forms_idForm) {
        this.forms_idForm = forms_idForm;
    }

    public String getLocals_adress() {
        return locals_adress;
    }

    public void setLocals_adress(String locals_adress) {
        this.locals_adress = locals_adress;
    }

    public String getUsers_username() {
        return users_username;
    }

    public void setUsers_username(String users_username) {
        this.users_username = users_username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
