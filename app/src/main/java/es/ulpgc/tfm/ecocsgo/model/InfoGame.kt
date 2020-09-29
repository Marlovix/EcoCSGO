package es.ulpgc.tfm.ecocsgo.model

class InfoGame {
    var grenadeDeaths: Int = 0
    var knifeDeaths: Int = 0
    var partnerDeaths: Int = 0
    var zeus: Int = 0
    var smoke: Int = 0
    var flash: Int = 0
    var he: Int = 0
    var molotov: Int = 0
    var decoy: Int = 0

    fun reset() {
        grenadeDeaths = 0
        knifeDeaths = 0
        partnerDeaths = 0
        zeus = 0
        smoke = 0
        flash = 0
        he = 0
        molotov = 0
        decoy = 0
    }
}