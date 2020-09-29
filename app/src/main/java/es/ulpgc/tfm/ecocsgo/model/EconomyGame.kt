package es.ulpgc.tfm.ecocsgo.model

class EconomyGame(
    val beginning: Int, val defeatBonus: ArrayList<Int>, val defuseBonus: Int,
    val explosionBonus: Int, val grenadeKill: Int, val killPartnerPenalty: Int,
    val knifeKill: Int, val leavingGame: Int, val max: Int, val plantBonus: Int,
    val victory: Map<TypeVictoryGameEnum, Int>
)