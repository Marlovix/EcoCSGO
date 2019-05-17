package es.ulpgc.tfm.ecocsgo.model;

abstract class Gun extends Equipment{
    Integer reward;
    Integer casualty;
    OriginEquipment origin;

    public Gun(String key) {
        super("weapons", key);
    }

}
