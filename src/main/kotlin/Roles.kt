object Roles {
    val merlin = Good("Merlin")
    val servantOfArthur = Good("Servant of Arthur")
    val percival = Good("Percival")

    val mordred = Evil("Mordred")
    val morgana = Evil("Morgana")
    val assassin = Evil("Assassin")
    val minionOfMordred = Evil("Minion of Mordred")
    val oberon = Evil("Oberon")

    fun Role.knowsMore(): Boolean =
            this === merlin
                    || this === percival
                    || (this !is Good && this !== oberon)
}