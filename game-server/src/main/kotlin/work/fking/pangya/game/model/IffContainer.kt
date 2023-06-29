package work.fking.pangya.game.model

interface IffContainer<I : IffObject> {
    val entries: List<I>

    fun findByIffId(iffId: Int): I? {
        return entries.find { it.iffId == iffId }
    }

    fun findByUid(uid: Int): I? {
        return entries.find { it.uid == uid }
    }

    fun existsByUid(uid: Int): Boolean {
        return findByUid(uid) != null
    }
}
