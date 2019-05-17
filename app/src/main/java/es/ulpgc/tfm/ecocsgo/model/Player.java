package es.ulpgc.tfm.ecocsgo.model;

import java.util.List;

public class Player {
    private List<MainGun> mainGuns;
    private List<SecondaryGun> secundaryGuns;
    private Vest vest;
    private Helmet helmet;
    private DefuseKit defuseKit;
    private boolean alive;

    public void registerMainGun(MainGun gun){
        mainGuns.add(gun);
    }
}
