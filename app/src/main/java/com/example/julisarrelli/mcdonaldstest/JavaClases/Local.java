package com.example.julisarrelli.mcdonaldstest.JavaClases;

/**
 * Created by julisarrelli on 1/10/17.
 */
public class Local {
    private int idLocal;
    private String adress;
    private String city;

    public Local(int idLocal,String adress,String city)
    {
        this.idLocal=idLocal;
        this.adress=adress;
        this.city=city;
    }

    public int getIdLocal()
    {
        return idLocal;
    }
    public String getAdress()
    {
        return adress;
    }
    public String getCity()
    {
        return city;
    }
}
